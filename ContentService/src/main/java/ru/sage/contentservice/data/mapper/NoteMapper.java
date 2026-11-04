package ru.sage.contentservice.data.mapper;

import lombok.experimental.UtilityClass;
import ru.sage.contentservice.data.model.NoteModel;
import ru.sage.contentservice.domain.model.NoteDomain;

import java.util.List;

@UtilityClass
public class NoteMapper {
    public static NoteDomain toDomain(NoteModel note) {

        if (note == null) {
            return null;
        }

        return new NoteDomain(
                note.uuid(),
                note.userId(),
                note.projectId(),
                note.title(),
                note.text(),
                note.createdAt(),
                note.updatedAt()
        );
    }

    public static List<NoteDomain> toDomain(List<NoteModel> notes) {
        return notes.stream().map(x -> new NoteDomain(
                x.uuid(),
                x.userId(),
                x.projectId(),
                x.title(),
                x.text(),
                x.createdAt(),
                x.updatedAt()
        )).toList();
    }
}
