package com.androidpoet.materialnotes.data.sync

import com.androidpoet.materialnotes.data.MainRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.androidpoet.supabase.client.Supabase
import io.github.androidpoet.supabase.database.DatabaseClient
import io.github.androidpoet.supabase.database.createDatabaseClient
import io.github.androidpoet.supabase.database.selectTyped
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
 * The Supabase client is created lazily and only when [SupabaseConfig.isConfigured], so an
 * un-configured build never constructs a client (which would throw on a blank URL/key).
 */
@Inject
@SingleIn(AppScope::class)
class NoteSyncService(
    private val repository: MainRepository,
    private val ioContext: CoroutineContext,
) {

    private val database: DatabaseClient? by lazy {
        if (!SupabaseConfig.isConfigured) {
            null
        } else {
            val client = Supabase.create(SupabaseConfig.PROJECT_URL, SupabaseConfig.ANON_KEY) {
                logging = true
            }
            createDatabaseClient(client)
        }
    }

    suspend fun sync(): SyncResult = withContext(ioContext) {
        val db = database ?: return@withContext SyncResult.NotConfigured

        // Push: upsert every local note up to Supabase (skipped when there's nothing local yet).
        val local = repository.getAllNotesOnce()
        if (local.isNotEmpty()) {
            val push = db.upsertTypedMany(
                table = SupabaseConfig.NOTES_TABLE,
                values = local.map { it.toRemote() },
                onConflict = "id",
            )
            push.errorOrNull()?.let {
                return@withContext SyncResult.Failure("Push failed: ${it.message}")
            }
        }

        // Pull: fetch the remote table and upsert each row into the local store.
        val pull = db.selectTyped<RemoteNote>(table = SupabaseConfig.NOTES_TABLE)
        val remote = pull.getOrNull()
            ?: return@withContext SyncResult.Failure(
                "Pull failed: ${pull.errorOrNull()?.message ?: "unknown error"}",
            )
        remote.forEach { repository.upsertNote(it.toDomain()) }

        SyncResult.Success(pushed = local.size, pulled = remote.size)
    }
}
