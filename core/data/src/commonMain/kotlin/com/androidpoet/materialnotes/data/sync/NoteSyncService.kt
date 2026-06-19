package com.androidpoet.materialnotes.data.sync

import com.androidpoet.materialnotes.data.MainRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.androidpoet.supabase.database.selectWithCount
import io.github.androidpoet.supabase.database.upsertTypedMany
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/** Outcome of a [NoteSyncService.sync] run, surfaced to the UI. */
sealed interface SyncResult {
    /** [SupabaseConfig] hasn't been filled in — cloud sync is a no-op until it is. */
    data object NotConfigured : SyncResult
    data class Success(val pushed: Int, val pulled: Int) : SyncResult
    data class Failure(val message: String) : SyncResult
}

/**
 * Two-way note sync against Supabase, built on AndroidPoet's own Supabase KMP SDK.
 *
 * [sync] pushes the local SQLDelight store up (upsert by id) and pulls the remote table back
 * down (upsert by id), so a note saved on any device converges everywhere. The app stays fully
 * usable offline; sync is an explicit, user-triggered action.
 *
 * Both directions are **paged** so the sync scales to a large notebook without ever holding the
 * whole remote table in one request: the push upserts in batches of [PAGE_SIZE], and the pull walks
 * PostgREST ranges via [selectWithCount] until the count-aware page comes back short.
 *
 * Requests go through the shared [SupabaseProvider] client, which carries the signed-in user's
 * access token — so the server scopes every row to its owner (`user_id` defaults to `auth.uid()`)
 * and a pull only ever returns the current user's own notes. Sync is a no-op until the user signs
 * in (no token → RLS matches nothing) or until [SupabaseConfig.isConfigured].
 */
@Inject
@SingleIn(AppScope::class)
class NoteSyncService(
    private val repository: MainRepository,
    private val supabase: SupabaseProvider,
    private val ioContext: CoroutineContext,
) {

    suspend fun sync(): SyncResult = withContext(ioContext) {
        val db = supabase.database ?: return@withContext SyncResult.NotConfigured

        // Push: upsert every local note up to Supabase, in batches so a big notebook is one
        // bounded request per page rather than a single huge body.
        val local = repository.getAllNotesOnce()
        for (batch in local.chunked(PAGE_SIZE)) {
            val push = db.upsertTypedMany(
                table = SupabaseConfig.NOTES_TABLE,
                values = batch.map { it.toRemote() },
                onConflict = "id",
            )
            push.errorOrNull()?.let {
                return@withContext SyncResult.Failure("Push failed: ${it.message}")
            }
        }

        // Pull: walk the remote table newest-first one page at a time, upserting each page into the
        // local store, until a page returns fewer rows than the page size (the last page).
        var pulled = 0
        var from = 0
        while (true) {
            val page = db.selectWithCount<RemoteNote>(table = SupabaseConfig.NOTES_TABLE) {
                order("created_at", ascending = false)
                range(from, from + PAGE_SIZE - 1)
            }
            val rows = page.getOrNull()?.rows
                ?: return@withContext SyncResult.Failure(
                    "Pull failed: ${page.errorOrNull()?.message ?: "unknown error"}",
                )
            rows.forEach { repository.upsertNote(it.toDomain()) }
            pulled += rows.size
            if (rows.size < PAGE_SIZE) break
            from += PAGE_SIZE
        }

        SyncResult.Success(pushed = local.size, pulled = pulled)
    }

    private companion object {
        /** Rows per page for both the batched push and the count-aware paged pull. */
        const val PAGE_SIZE = 100
    }
}
