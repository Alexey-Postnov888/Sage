package ru.sage.contentservice.domain.usecase.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sage.contentservice.domain.model.ProjectDomain;
import ru.sage.contentservice.domain.repository.NoteRepository;
import ru.sage.contentservice.domain.repository.ProjectRepository;
import ru.sage.contentservice.domain.usecase.ProjectUseCase;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectUseCaseImpl implements ProjectUseCase {

    private final ProjectRepository repository;

    @Autowired
    ProjectUseCaseImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public int createProject(String userId, UUID workspaceId, String title) {
        return repository.createProject(userId, workspaceId, title);
    }

    @Override
    public List<ProjectDomain> getAllProjects() {
        return repository.getAllProjects();
    }

    @Override
    public ProjectDomain getProjectById(UUID id) {
        return repository.getProjectById(id);
    }

    @Override
    public int updateProject(String userId, UUID workspaceId, String title, UUID id) {
        return repository.updateProject(userId, workspaceId, title, id);
    }

    @Override
    public int deleteProject(UUID id) {
        return repository.deleteProject(id);
    }
}
