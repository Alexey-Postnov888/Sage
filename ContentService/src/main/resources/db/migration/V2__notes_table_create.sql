CREATE TABLE IF NOT EXISTS notes
(
    id uuid NOT NULL DEFAULT uuidv7(),
    user_id character varying COLLATE pg_catalog."default",
    project_id uuid NOT NULL,
    title character varying COLLATE pg_catalog."default",
    text character varying COLLATE pg_catalog."default",
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
                             CONSTRAINT notes_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS notes
    OWNER to content_user;

CREATE TRIGGER before_notes_update
    BEFORE UPDATE
    ON notes
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();