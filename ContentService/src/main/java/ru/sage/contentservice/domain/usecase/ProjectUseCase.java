package ru.sage.contentservice.domain.usecase;

import ru.sage.contentservice.domain.model.ProjectDomain;

import java.util.List;
import java.util.UUID;

public interface ProjectUseCase {

    int createProject(String userId, UUID workspaceId, String title);
    List<ProjectDomain> getAllProjects();
    ProjectDomain getProjectById(UUID id);
    int updateProject(String userId, UUID workspaceId, String title, UUID id);
    int deleteProject(UUID id);
}
