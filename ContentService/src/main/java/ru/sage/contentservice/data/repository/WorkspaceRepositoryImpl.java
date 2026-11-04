package ru.sage.contentservice.data.repository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.sage.contentservice.data.model.db.WorkspaceEntity;
import ru.sage.contentservice.domain.model.WorkspaceDomain;
import ru.sage.contentservice.domain.repository.WorkspaceRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final DSLContext dslContext;

    @Autowired
    WorkspaceRepositoryImpl(DSLContext dslContext) { this.dslContext = dslContext; }

    @Override
    public List<WorkspaceDomain> getAllWorkspaces() {
        return dslContext.select().from(WorkspaceEntity.TABLE)
                .fetchInto(WorkspaceDomain.class);
    }

    @Override
    public WorkspaceDomain getWorkspaceById(UUID id) {
        return dslContext.select().from(WorkspaceEntity.TABLE)
                .where(WorkspaceEntity.id.equal(id))
                .fetchOneInto(WorkspaceDomain.class);
    }

    @Override
    public int createWorkspace(String userId, String title) {
        return dslContext.insertInto(WorkspaceEntity.TABLE)
                .set(WorkspaceEntity.userId, userId)
                .set(WorkspaceEntity.title, title)
                .execute();
    }

    @Override
    public int updateWorkspace(String userId, String title, UUID id) {
        var update = dslContext.updateQuery(WorkspaceEntity.TABLE);

        if (userId != null) {
            update.addValue(WorkspaceEntity.userId, userId);
        }
        if (title != null) {
            update.addValue(WorkspaceEntity.title, title);
        }

        update.addConditions(WorkspaceEntity.id.equal(id));
        return update.execute();
    }

    @Override
    public int deleteWorkspace(UUID id) {
        return dslContext.delete(WorkspaceEntity.TABLE)
                .where(WorkspaceEntity.id.equal(id))
                .execute();
    }
}
