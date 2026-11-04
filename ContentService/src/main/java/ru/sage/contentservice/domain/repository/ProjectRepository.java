package ru.sage.contentservice.domain.repository;


import ru.sage.contentservice.domain.model.ProjectDomain;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository {

    List<ProjectDomain> getAllProjects();
    ProjectDomain getProjectById(UUID id);
    int createProject(String userId, UUID workspaceId, String title);
    int updateProject(String userId, UUID workspaceId, String title, UUID id);
    int deleteProject(UUID id);
}
