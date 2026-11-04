package ru.sage.contentservice.domain.usecase.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sage.contentservice.domain.model.NoteDomain;
import ru.sage.contentservice.domain.repository.NoteRepository;
import ru.sage.contentservice.domain.usecase.NoteUseCase;

import java.util.List;
import java.util.UUID;

@Service
public class NoteUseCaseImpl implements NoteUseCase {
    
    private final NoteRepository repository;
    
    @Autowired
    NoteUseCaseImpl(NoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public int createNote(String userId, UUID projectId, String title, String text) {
        return repository.createNote(userId, projectId, title, text);
    }

    @Override
    public List<NoteDomain> getAllNotes() {
        return repository.getAllNotes();
    }

    @Override
    public NoteDomain getNoteById(UUID id) {
        return repository.getNoteById(id);
    }

    @Override
    public int updateNote(String userId, UUID projectId, String title, String text, UUID id) {
        return repository.updateNote(userId, projectId, title, text, id);
    }

    @Override
    public int deleteNote(UUID id) {
        return repository.deleteNote(id);
    }
}
