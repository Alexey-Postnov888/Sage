package ru.sage.contentservice.data.model.db;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProjectEntity {

    public static final Table<?> TABLE = DSL.table("projects");

    public static final Field<UUID> id = DSL.field("id", UUID.class);

    public static final Field<String> userId = DSL.field("user_id", String.class);

    public static final Field<UUID> workspaceId = DSL.field("workspace_id", UUID.class);

    public static final Field<String> title = DSL.field("title", String.class);

    public static final Field<LocalDateTime> createdAt = DSL.field("created_at", LocalDateTime.class);

    public static final Field<LocalDateTime> updatedAt = DSL.field("updated_at", LocalDateTime.class);
}
