package ru.sage.contentservice.presentation.mapper;

import lombok.experimental.UtilityClass;
import ru.sage.contentservice.domain.model.NoteDomain;
import ru.sage.contentservice.presentation.model.NoteResponse;

import java.util.List;

@UtilityClass
public class NoteMapper {
    public static NoteResponse toPresentation(NoteDomain note) {

        if (note == null) {
            return null;
        }

        return new NoteResponse(
                note.id(),
                note.userId(),
                note.projectId(),
                note.title(),
                note.text(),
                note.createdAt(),
                note.updatedAt()
        );
    }

    public static List<NoteResponse> toPresentation(List<NoteDomain> notes) {
        return notes.stream().map(x -> new NoteResponse(
                x.id(),
                x.userId(),
                x.projectId(),
                x.title(),
                x.text(),
                x.createdAt(),
                x.updatedAt()
        )).toList();
    }
}
