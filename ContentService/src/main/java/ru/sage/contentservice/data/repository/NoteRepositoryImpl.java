package ru.sage.contentservice.data.repository;


import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.jooq.DSLContext;
import ru.sage.contentservice.data.mapper.NoteMapper;
import ru.sage.contentservice.data.model.db.NoteEntity;
import ru.sage.contentservice.data.model.NoteModel;
import ru.sage.contentservice.domain.model.NoteDomain;
import ru.sage.contentservice.domain.repository.NoteRepository;

import java.util.List;
import java.util.UUID;

@Repository
@ExtensionMethod(NoteMapper.class)
public class NoteRepositoryImpl implements NoteRepository {

    private final DSLContext dslContext;

    @Autowired
    NoteRepositoryImpl(DSLContext dslContext) { this.dslContext = dslContext; }

    @Override
    public List<NoteDomain> getAllNotes() {
        return dslContext.select().from(NoteEntity.TABLE)
                .fetchInto(NoteModel.class).toDomain();
    }

    @Override
    public NoteDomain getNoteById(UUID id) {
        NoteModel note = dslContext.select().from(NoteEntity.TABLE)
                .where(NoteEntity.id.equal(id))
                .fetchOneInto(NoteModel.class);

        if (note==null) {
            return null;
        }
        return note.toDomain();
    }

    @Override
    public int createNote(String userId, UUID projectId, String title, String text) {
        return dslContext.insertInto(NoteEntity.TABLE)
                .set(NoteEntity.userId, userId)
                .set(NoteEntity.projectId, projectId)
                .set(NoteEntity.title, title)
                .set(NoteEntity.text, text)
                .execute();
    }

    @Override
    public int updateNote(String userId, UUID projectId, String title, String text, UUID id) {

        var update = dslContext.updateQuery(NoteEntity.TABLE);

        if (userId != null) {
            update.addValue(NoteEntity.userId, userId);
        }
        if (projectId != null) {
            update.addValue(NoteEntity.projectId, projectId);
        }
        if (title != null) {
            update.addValue(NoteEntity.title, title);
        }
        if (text != null) {
            update.addValue(NoteEntity.text, text);
        }

        update.addConditions(NoteEntity.id.equal(id));
        return update.execute();
    }

    @Override
    public int deleteNote(UUID id) {
        return dslContext.delete(NoteEntity.TABLE)
                .where(NoteEntity.id.equal(id))
                .execute();
    }
}
