package ru.sage.contentservice.presentation.mapper;

import lombok.experimental.UtilityClass;
import ru.sage.contentservice.domain.model.ProjectDomain;
import ru.sage.contentservice.presentation.model.ProjectResponse;

import java.util.List;

@UtilityClass
public class ProjectMapper {
    public static ProjectResponse toPresentation(ProjectDomain project) {

        if (project == null) {
            return null;
        }

        return new ProjectResponse(
                project.id(),
                project.userId(),
                project.workspaceId(),
                project.title(),
                project.createdAt(),
                project.updatedAt()
        );
    }

    public static List<ProjectResponse> toPresentation(List<ProjectDomain> projects) {
        return projects.stream().map(x -> new ProjectResponse(
                x.id(),
                x.userId(),
                x.workspaceId(),
                x.title(),
                x.createdAt(),
                x.updatedAt()
        )).toList();
    }
}
