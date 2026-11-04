package ru.sage.contentservice.presentation.router;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sage.contentservice.domain.usecase.ProjectUseCase;
import ru.sage.contentservice.presentation.mapper.ProjectMapper;
import ru.sage.contentservice.presentation.model.ProjectRequest;
import ru.sage.contentservice.presentation.model.ProjectResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/project")
@ExtensionMethod(ProjectMapper.class)
public class ProjectController {

    private final ProjectUseCase projectUseCase;

    @Autowired
    ProjectController(ProjectUseCase projectUseCase) { this.projectUseCase = projectUseCase; }

    @GetMapping("/")
    public List<ProjectResponse> getAllProjects() {

        return projectUseCase.getAllProjects().toPresentation();
    }

    @GetMapping("/{project_id}")
    public ProjectResponse getProjectById(@PathVariable UUID project_id) {
        return projectUseCase.getProjectById(project_id).toPresentation();
    }

    @PostMapping("/create")
    public int createProject(@RequestBody ProjectRequest project) {
        return projectUseCase.createProject(project.userId(), project.workspaceId(), project.title());
    }

    @PostMapping("/update/{project_id}")
    public int updateProject(@RequestBody ProjectRequest project, @PathVariable UUID project_id) {
        return projectUseCase.updateProject(project.userId(), project.workspaceId(), project.title(), project_id);
    }

    @DeleteMapping("/delete/{project_id}")
    public int deleteProject(@PathVariable UUID project_id) {

        return projectUseCase.deleteProject(project_id);
    }
}
