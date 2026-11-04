package ru.sage.contentservice.domain.repository;

import ru.sage.contentservice.domain.model.NoteDomain;

import java.util.List;
import java.util.UUID;

public interface NoteRepository {

    List<NoteDomain> getAllNotes();
    NoteDomain getNoteById(UUID id);
    int createNote(String userId, UUID projectId, String title, String text);
    int updateNote(String userId, UUID projectId, String title, String text, UUID id);
    int deleteNote(UUID id);
}
