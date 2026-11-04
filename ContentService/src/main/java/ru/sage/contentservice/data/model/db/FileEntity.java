package ru.sage.contentservice.data.model.db;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.util.UUID;

public class FileEntity {

    public static final Table<?> TABLE = DSL.table("files");

    public static final Field<UUID> id = DSL.field("id", UUID.class);

    public static final Field<UUID> noteId = DSL.field("note_id", UUID.class);

    public static final Field<String> fid = DSL.field("fid", String.class);

    public static final Field<String> filename = DSL.field("filename", String.class);

    public static final Field<String> key = DSL.field("key", String.class);

    public static final Field<String> url = DSL.field("url", String.class);

    public static final Field<String> extension = DSL.field("extension", String.class);

    public static final Field<String> mimeType = DSL.field("mime_type", String.class);

    public static final Field<Long> size = DSL.field("size", Long.class);

    public static final Field<LocalDateTime> createdAt = DSL.field("created_at", LocalDateTime.class);

    public static final Field<LocalDateTime> updatedAt = DSL.field("updated_at", LocalDateTime.class);
}
