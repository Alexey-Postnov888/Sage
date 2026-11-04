package ru.sage.contentservice.presentation.mapper;

import lombok.experimental.UtilityClass;
import ru.sage.contentservice.domain.model.ProjectDomain;
import ru.sage.contentservice.domain.model.WorkspaceDomain;
import ru.sage.contentservice.presentation.model.ProjectResponse;
import ru.sage.contentservice.presentation.model.WorkspaceResponse;

import java.util.List;

@UtilityClass
public class WorkspaceMapper {

    public static WorkspaceResponse toPresentation(WorkspaceDomain workspace) {

        if (workspace == null) {
            return null;
        }

        return new WorkspaceResponse(
                workspace.id(),
                workspace.userId(),
                workspace.title(),
                workspace.createdAt(),
                workspace.updatedAt()
        );
    }

    public static List<WorkspaceResponse> toPresentation(List<WorkspaceDomain> workspaces) {
        return workspaces.stream().map(x -> new WorkspaceResponse(
                x.id(),
                x.userId(),
                x.title(),
                x.createdAt(),
                x.updatedAt()
        )).toList();
    }
}
