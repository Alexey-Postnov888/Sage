package ru.sage.contentservice.domain.repository;

import ru.sage.contentservice.domain.model.WorkspaceDomain;

import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository {

    List<WorkspaceDomain> getAllWorkspaces();
    WorkspaceDomain getWorkspaceById(UUID id);
    int createWorkspace(String userId, String title);
    int updateWorkspace(String userId, String title, UUID id);
    int deleteWorkspace(UUID id);
}
