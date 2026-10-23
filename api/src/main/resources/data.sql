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

CREATE INDEX IF NOT EXISTS idx_users_search_vector
    ON users USING GIN (search_vector);

-- Need to drop then add in case constraint already exists
ALTER TABLE users DROP CONSTRAINT IF EXISTS fk_users_avatar;

ALTER TABLE users
    ADD CONSTRAINT fk_users_avatar
    FOREIGN KEY (avatar_id) REFERENCES avatars (src);