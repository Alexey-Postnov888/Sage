package ru.sage.contentservice.data.repository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.sage.contentservice.data.model.db.ProjectEntity;
import ru.sage.contentservice.domain.model.ProjectDomain;
import ru.sage.contentservice.domain.repository.ProjectRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final DSLContext dslContext;

    @Autowired
    ProjectRepositoryImpl(DSLContext dslContext) { this.dslContext = dslContext; }

    @Override
    public List<ProjectDomain> getAllProjects() {
        return dslContext.select().from(ProjectEntity.TABLE)
                .fetchInto(ProjectDomain.class);
    }

    @Override
    public ProjectDomain getProjectById(UUID id) {

        return dslContext.select().from(ProjectEntity.TABLE)
                .where(ProjectEntity.id.equal(id))
                .fetchOneInto(ProjectDomain.class);
    }

    @Override
    public int createProject(String userId, UUID workspaceId, String title) {

        return dslContext.insertInto(ProjectEntity.TABLE)
                .set(ProjectEntity.userId, userId)
                .set(ProjectEntity.workspaceId, workspaceId)
                .set(ProjectEntity.title, title)
                .execute();
    }

    @Override
    public int updateProject(String userId, UUID workspaceId, String title, UUID id) {

        var update = dslContext.updateQuery(ProjectEntity.TABLE);

        if (userId != null) {
            update.addValue(ProjectEntity.userId, userId);
        }
        if (workspaceId != null) {
            update.addValue(ProjectEntity.workspaceId, workspaceId);
        }
        if (title != null) {
            update.addValue(ProjectEntity.title, title);
        }

        update.addConditions(ProjectEntity.id.equal(id));
        return update.execute();
    }

    @Override
    public int deleteProject(UUID id) {

        return dslContext.delete(ProjectEntity.TABLE)
                .where(ProjectEntity.id.equal(id))
                .execute();
    }
}
