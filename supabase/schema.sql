-- Material Notes — Supabase schema for cloud sync.
--
-- Run this in your Supabase project's SQL Editor, then set PROJECT_URL + ANON_KEY in
-- core/data/.../data/sync/SupabaseConfig.kt. Columns mirror the local SQLDelight `Note`
-- table one-to-one (see core/data/.../db/Note.sq) so a row round-trips losslessly.

create table if not exists public.notes (
    id        bigint primary key,
    title     text   not null default '',
    date      text   not null default '',
    backround bigint not null default 0,
    content   text   not null default ''
);

-- ─────────────────────────────────────────────────────────────────────────────
-- Anon-key setup (current app default — one shared notebook, no sign-in)
--
-- The app ships with the anon/publishable key only, so grant that role access.
-- This makes every client share the same `notes` table. Fine for a single user
-- or a trusted team; do NOT use it for untrusted multi-tenant data.
alter table public.notes enable row level security;

create policy "anon can read notes"  on public.notes for select using (true);
create policy "anon can write notes" on public.notes for insert with check (true);
create policy "anon can update notes" on public.notes for update using (true) with check (true);

-- ─────────────────────────────────────────────────────────────────────────────
-- Per-user notes (upgrade path — each dev user keeps their own notebook)
--
-- To make notes private per signed-in user, add the Supabase auth module to the
-- app, add a `user_id uuid` column, and replace the policies above with:
--
--   alter table public.notes add column user_id uuid not null default auth.uid();
--   drop policy "anon can read notes"   on public.notes;
--   drop policy "anon can write notes"  on public.notes;
--   drop policy "anon can update notes" on public.notes;
--   create policy "owner can read"   on public.notes for select using (auth.uid() = user_id);
--   create policy "owner can write"  on public.notes for insert with check (auth.uid() = user_id);
--   create policy "owner can update" on public.notes for update using (auth.uid() = user_id);
--
-- NEVER embed the service_role key in the client — it bypasses every policy above.
