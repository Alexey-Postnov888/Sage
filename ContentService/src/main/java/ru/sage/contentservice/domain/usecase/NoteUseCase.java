package ru.sage.contentservice.domain.usecase;

import org.springframework.web.bind.annotation.PathVariable;
import ru.sage.contentservice.domain.model.NoteDomain;
import ru.sage.contentservice.presentation.model.NoteRequest;

import java.util.List;
import java.util.UUID;

public interface NoteUseCase {

    int createNote(String userId, UUID projectId, String title, String text);
    List<NoteDomain> getAllNotes();
    NoteDomain getNoteById(UUID id);
    int updateNote(String userId, UUID projectId, String title, String text, UUID id);
    int deleteNote(UUID id);
}

