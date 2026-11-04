package ru.sage.contentservice.domain.usecase;

import ru.sage.contentservice.domain.model.WorkspaceDomain;

import java.util.List;
import java.util.UUID;

public interface WorkspaceUseCase {

    int createWorkspace(String userId, String title);
    List<WorkspaceDomain> getAllWorkspaces();
    WorkspaceDomain getWorkspaceById(UUID id);
    int updateWorkspace(String userId, String title, UUID id);
    int deleteWorkspace(UUID id);
}
