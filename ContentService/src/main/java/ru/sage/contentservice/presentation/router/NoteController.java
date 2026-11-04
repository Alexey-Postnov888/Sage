package ru.sage.contentservice.presentation.router;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sage.contentservice.domain.usecase.NoteUseCase;
import ru.sage.contentservice.presentation.mapper.NoteMapper;
import ru.sage.contentservice.presentation.model.NoteRequest;
import ru.sage.contentservice.presentation.model.NoteResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
@ExtensionMethod(NoteMapper.class)
public class NoteController {

    private final NoteUseCase noteUseCase;

    @Autowired
    NoteController(NoteUseCase noteUseCase) {
        this.noteUseCase = noteUseCase;
    }

    @GetMapping("/")
    public List<NoteResponse> getAllNotes() {
        return noteUseCase.getAllNotes().toPresentation();
    }

    @GetMapping("/{note_id}")
    public NoteResponse getNoteById(@PathVariable UUID note_id) {
        return noteUseCase.getNoteById(note_id).toPresentation();
    }

    @PostMapping("/create")
    public int createNote(@RequestBody NoteRequest note) {
        return noteUseCase.createNote(note.userId(), note.projectId(), note.title(), note.text());
    }

    @PostMapping("/update/{note_id}")
    public int updateNote(@RequestBody NoteRequest note, @PathVariable UUID note_id) {
        return noteUseCase.updateNote(note.userId(), note.projectId(), note.title(), note.text(), note_id);
    }

    @DeleteMapping("/delete/{note_id}")
    public int deleteNote(@PathVariable UUID note_id) {
        return noteUseCase.deleteNote(note_id);
    }
}
