CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    auth_subject TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    first_name TEXT,
    last_name TEXT,
    username TEXT UNIQUE
);

ALTER TABLE users
ADD COLUMN IF NOT EXISTS search_vector tsvector
GENERATED ALWAYS AS (
    to_tsvector(
        'simple',
        regexp_replace(coalesce(username, ''), '[^[:alnum:]]+', ' ', 'g')
        || ' ' || coalesce(first_name, '')
        || ' ' || coalesce(last_name, '')
    )
) STORED;

CREATE INDEX IF NOT EXISTS idx_users_search_vector ON users USING GIN (search_vector);

CREATE TYPE challenge_status AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETE');

CREATE TABLE challenges (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    owner_id BIGINT REFERENCES users NOT NULL,
    name TEXT NOT NULL,
    start_date DATE,
    duration_days INTEGER,
    status challenge_status NOT NULL,
    CONSTRAINT uq_challenges_owner_name UNIQUE (owner_id, name)
);

CREATE TABLE participants (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users NOT NULL,
    challenge_id BIGINT REFERENCES challenges NOT NULL,
    ready BOOLEAN NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT now() -- Hibernate should manage timestamp but including default as a safety net
);

CREATE TYPE invite_status AS ENUM ('PENDING', 'ACCEPTED', 'DECLINED');

CREATE TABLE invites (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    challenge_id BIGINT REFERENCES challenges NOT NULL,
    invite_sender_id BIGINT REFERENCES users NOT NULL,
    invited_user_id BIGINT REFERENCES users NOT NULL,
    status invite_status NOT NULL DEFAULT 'PENDING', -- JPA entity has default state but including default as a safety net
    CONSTRAINT uq_challenge_id_invited_user_id UNIQUE (challenge_id, invited_user_id)
);

CREATE TABLE goal_definitions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    participant_id BIGINT REFERENCES participants NOT NULL,
    name TEXT NOT NULL
);

CREATE UNIQUE INDEX uq_goal_definitions_participant_name_lower
    ON goal_definitions (participant_id, lower(name));

CREATE TABLE goal_completions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    goal_definition_id BIGINT REFERENCES goal_definitions NOT NULL,
    completed_date DATE NOT NULL,
    CONSTRAINT uq_goal_definition_id_completed_date UNIQUE (goal_definition_id, completed_date)
);
