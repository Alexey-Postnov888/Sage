CREATE TABLE IF NOT EXISTS files
(
    id uuid NOT NULL DEFAULT uuidv7(),
    note_id uuid NOT NULL,
    fid character varying COLLATE pg_catalog."default" NOT NULL DEFAULT ''::character varying,
    filename character varying COLLATE pg_catalog."default" NOT NULL,
    bucket character varying COLLATE pg_catalog."default" NOT NULL,
    key character varying COLLATE pg_catalog."default" NOT NULL,
    url character varying COLLATE pg_catalog."default" NOT NULL DEFAULT ''::character varying,
    extension character varying COLLATE pg_catalog."default" NOT NULL,
    mime_type character varying COLLATE pg_catalog."default" NOT NULL,
    size bigint NOT NULL DEFAULT 0,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
                             CONSTRAINT files_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS files
    OWNER to content_user;

CREATE TRIGGER before_files_update
    BEFORE UPDATE
    ON files
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();