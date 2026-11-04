CREATE TABLE IF NOT EXISTS workspaces
(
    id uuid NOT NULL DEFAULT uuidv7(),
    user_id character varying COLLATE pg_catalog."default" NOT NULL,
    title character varying COLLATE pg_catalog."default",
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT workspaces_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS workspaces
    OWNER to content_user;


CREATE TABLE IF NOT EXISTS projects
(
    id uuid NOT NULL DEFAULT uuidv7(),
    user_id character varying COLLATE pg_catalog."default" NOT NULL,
    workspace_id uuid NOT NULL,
    title character varying COLLATE pg_catalog."default",
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT projects_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS projects
    OWNER to content_user;


CREATE FUNCTION set_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
	NEW.updated_at := now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_workspaces_update
    BEFORE UPDATE
    ON workspaces
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER before_projects_update
    BEFORE UPDATE
    ON projects
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();