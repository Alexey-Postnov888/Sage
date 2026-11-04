package ru.sage.contentservice.presentation.router;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sage.contentservice.domain.usecase.WorkspaceUseCase;
import ru.sage.contentservice.presentation.mapper.WorkspaceMapper;
import ru.sage.contentservice.presentation.model.WorkspaceRequest;
import ru.sage.contentservice.presentation.model.WorkspaceResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workspace")
@ExtensionMethod(WorkspaceMapper.class)
public class WorkspaceController {

    private final WorkspaceUseCase workspaceUseCase;

    @Autowired
    WorkspaceController(WorkspaceUseCase workspaceUseCase) { this.workspaceUseCase = workspaceUseCase; }

    @GetMapping("/")
    public List<WorkspaceResponse> getAllWorkspaces() {

        return workspaceUseCase.getAllWorkspaces().toPresentation();
    }

    @GetMapping("/{workspace_id}")
    public WorkspaceResponse getWorkspaceById(@PathVariable UUID workspace_id) {
        return workspaceUseCase.getWorkspaceById(workspace_id).toPresentation();
    }

    @PostMapping("/create")
    public int createWorkspace(@RequestBody WorkspaceRequest workspace) {
        return workspaceUseCase.createWorkspace(workspace.userId(), workspace.title());
    }

    @PostMapping("/update/{workspace_id}")
    public int updateWorkspace(@RequestBody WorkspaceRequest workspace, @PathVariable UUID workspace_id) {
        return workspaceUseCase.updateWorkspace(workspace.userId(), workspace.title(), workspace_id);
    }

    @DeleteMapping("/delete/{workspace_id}")
    public int deleteWorkspace(@PathVariable UUID workspace_id) {

        return workspaceUseCase.deleteWorkspace(workspace_id);
    }
}
