package ru.sage.contentservice.domain.repository;

import org.springframework.web.multipart.MultipartFile;
import ru.sage.contentservice.domain.model.FileDomain;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileRepository {

    void createFile(UUID noteId, String filename, String bucket, String key, String extension, String mimeType, long size);
    List<FileDomain> getAllFiles();
    FileDomain getFileById(UUID id);
    void deleteFile(UUID id);
    String getFileKey(UUID id);
}
