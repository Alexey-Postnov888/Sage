package ru.sage.contentservice.domain.usecase.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sage.contentservice.domain.model.WorkspaceDomain;
import ru.sage.contentservice.domain.repository.WorkspaceRepository;
import ru.sage.contentservice.domain.usecase.WorkspaceUseCase;

import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceUseCaseImpl implements WorkspaceUseCase {

    private final WorkspaceRepository repository;

    @Autowired
    WorkspaceUseCaseImpl(WorkspaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public int createWorkspace(String userId, String title) {
        return repository.createWorkspace(userId, title);
    }

    @Override
    public List<WorkspaceDomain> getAllWorkspaces() {
        return repository.getAllWorkspaces();
    }

    @Override
    public WorkspaceDomain getWorkspaceById(UUID id) {
        return repository.getWorkspaceById(id);
    }

    @Override
    public int updateWorkspace(String userId, String title, UUID id) {
        return repository.updateWorkspace(userId, title, id);
    }

    @Override
    public int deleteWorkspace(UUID id) {
        return repository.deleteWorkspace(id);
    }
}
