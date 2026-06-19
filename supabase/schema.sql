-- Material Notes — Supabase schema for per-user cloud sync.
--
-- This is the schema that backs the live demo this app ships against. It mirrors the
-- local SQLDelight `Note` table one-to-one (see core/data/.../db/Note.sq) plus a
-- `user_id` column so every signed-in user keeps their own private notebook.
--
-- Apply it with the Supabase CLI (`supabase db push`) or paste it into the SQL Editor.
-- The app reads PROJECT_URL + the public anon key from
-- core/data/.../data/sync/SupabaseConfig.kt and authenticates each request with the
-- signed-in user's JWT, so the policies below scope every row to its owner.

create table if not exists public.notes (
    id         uuid   primary key,                       -- client-generated UUID (Uuid.random() on device)
    user_id    uuid   not null default auth.uid()         -- owner; defaults to the caller's auth.uid()
                       references auth.users (id) on delete cascade,
    title      text   not null default '',
    date       text   not null default '',
    backround  bigint not null default 0,
    content    text   not null default '',
    created_at bigint not null default 0                  -- epoch millis at creation; drives newest-first order
);

create index if not exists notes_user_id_idx on public.notes (user_id);

-- ─────────────────────────────────────────────────────────────────────────────
-- Per-user Row Level Security
--
-- Every request carries the signed-in user's JWT (the app ships only the public
-- anon key; the user_id is taken from auth.uid()), so each user can see and touch
-- only their own rows. The anon role — used before sign-in — matches nothing here.
--
-- NEVER embed the service_role key in the client: it bypasses every policy below.
alter table public.notes enable row level security;

drop policy if exists "owner can read"   on public.notes;
drop policy if exists "owner can insert" on public.notes;
drop policy if exists "owner can update" on public.notes;
drop policy if exists "owner can delete" on public.notes;

create policy "owner can read"   on public.notes for select using (auth.uid() = user_id);
create policy "owner can insert" on public.notes for insert with check (auth.uid() = user_id);
create policy "owner can update" on public.notes for update using (auth.uid() = user_id) with check (auth.uid() = user_id);
create policy "owner can delete" on public.notes for delete using (auth.uid() = user_id);
