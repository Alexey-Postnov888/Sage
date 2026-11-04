package ru.sage.contentservice.data.repository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.sage.contentservice.data.model.db.FileEntity;
import ru.sage.contentservice.domain.model.FileDomain;
import ru.sage.contentservice.domain.repository.FileRepository;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Repository
public class FileRepositoryImpl implements FileRepository {

    private final DSLContext dslContext;

    @Autowired
    FileRepositoryImpl(DSLContext dslContext) { this.dslContext = dslContext; }

    @Override
    public void createFile(UUID noteId, String filename, String bucket, String key, String extension, String mimeType, long size) {
        dslContext.insertInto(FileEntity.TABLE)
                .set(FileEntity.noteId, noteId)
                .set(FileEntity.filename, filename)
                .set(FileEntity.bucket, bucket)
                .set(FileEntity.key, key)
                .set(FileEntity.extension, extension)
                .set(FileEntity.mimeType, mimeType)
                .set(FileEntity.size, size)
                .execute();
    }

    @Override
    public List<FileDomain> getAllFiles() {
        return dslContext.select()
                .from(FileEntity.TABLE)
                .fetchInto(FileDomain.class);
    }

    @Override
    public FileDomain getFileById(UUID id) {
        return dslContext.select()
                .from(FileEntity.TABLE)
                .where(FileEntity.id.equal(id))
                .fetchOneInto(FileDomain.class);
    }

    @Override
    public void deleteFile(UUID id) {
        dslContext.delete(FileEntity.TABLE)
                .where(FileEntity.id.equal(id))
                .execute();
    }

    @Override
    public String getFileKey(UUID id) {
        return dslContext.select()
                .from(FileEntity.TABLE)
                .where(FileEntity.id.equal(id))
                .fetchSingle(FileEntity.key, String.class);
    }
}
