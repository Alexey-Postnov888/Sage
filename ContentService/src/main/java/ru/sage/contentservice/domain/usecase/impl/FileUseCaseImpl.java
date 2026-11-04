package ru.sage.contentservice.domain.usecase.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.sage.contentservice.data.service.SeaweedFsService;
import ru.sage.contentservice.domain.model.FileDomain;
import ru.sage.contentservice.domain.repository.FileRepository;
import ru.sage.contentservice.domain.repository.NoteRepository;
import ru.sage.contentservice.domain.usecase.FileUseCase;
import ru.sage.contentservice.presentation.model.FileInfoResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class FileUseCaseImpl implements FileUseCase {

    private final SeaweedFsService service;
    private final FileRepository fileRepository;
    private final NoteRepository noteRepository;

    @Autowired
    public FileUseCaseImpl(SeaweedFsService service, FileRepository fileRepository, NoteRepository noteRepository) {

        this.service = service;
        this.fileRepository = fileRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public String uploadFile(UUID noteId, String userid, MultipartFile file) {

        try {
            //Достаём инфу
            UUID projectId = noteRepository.getNoteById(noteId).projectId();

            //id проекта - название бакета
            String bucket = projectId.toString();

            //Берём данные для создания ключа + создание модели бд
            UUID randomUuid = UUID.randomUUID();
            String filename = file.getOriginalFilename().split("\\.")[0];
            String extension = file.getOriginalFilename().split("\\.")[1];
            String key = noteId + "/" + filename + "_" + randomUuid + "." + extension;
            String mimeType = file.getContentType();
            long size = file.getSize();

            //Проверяем существование бакета и создаём его
            if (!service.bucketExists(bucket)) {
                service.createBucket(bucket);
            }

            //Грузим файл
            service.upload(bucket, key, file);

            //Сохраняем инфу о файле в бд
            fileRepository.createFile(noteId, filename, key, extension, mimeType, size);

            return key;
        }
        catch (IOException e) {
            System.out.println("Ошибка IO");
        }
        catch (Exception e) {
            System.out.println("Ошибка Exception" + e.getMessage());
        }

        return null;
    }

    @Override
    public StreamingResponseBody downloadFile(UUID id) {

        FileDomain file = fileRepository.getFileById(id);
        String bucket = noteRepository.getNoteById(file.noteId()).projectId().toString();
        String key = file.key();

        return service.download(bucket, key);
    }

    @Override
    public String getPresignedUrl(UUID id) {

        FileDomain file = fileRepository.getFileById(id);

        String bucket = noteRepository.getNoteById(file.noteId()).projectId().toString();
        String key = file.key();

        return service.getPresignedUrl(bucket, key);
    }

    @Override
    public List<FileDomain> getAllFilesInfo() {
        return fileRepository.getAllFiles();
    }

    @Override
    public FileDomain getFileInfoById(UUID id) {
        return fileRepository.getFileById(id);
    }

    @Override
    public String getEncodedFilename(UUID id) {

        String fileName = fileRepository.getFileById(id).filename();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        return encodedFileName;
    }

    @Override
    public HeadObjectResponse getHead(UUID id) {
        FileDomain file = fileRepository.getFileById(id);

        String bucket = noteRepository.getNoteById(file.noteId()).projectId().toString();
        String key = file.key();

        return service.getHead(bucket, key);
    }

    @Override
    public List<String> listFiles(String bucket) {

        return service.list(bucket);
    }

    @Override
    public void deleteFile(UUID id) {

        FileDomain file = fileRepository.getFileById(id);

        String bucket = noteRepository.getNoteById(file.noteId()).projectId().toString();
        String key = file.key();

        fileRepository.deleteFile(id);
        service.delete(bucket, key);
    }

    @Override
    public String getFileKey(UUID id) {
        return fileRepository.getFileKey(id);
    }
}
