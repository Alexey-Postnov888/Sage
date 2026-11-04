package ru.sage.contentservice.domain.usecase;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.sage.contentservice.domain.model.FileDomain;
import ru.sage.contentservice.presentation.model.FileInfoResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileUseCase {
    String uploadFile(UUID noteId, String userId, MultipartFile file) throws IOException;
    StreamingResponseBody downloadFile(UUID id);
    String getPresignedUrl(UUID id);
    List<FileDomain> getAllFilesInfo();
    FileDomain getFileInfoById(UUID id);
    String getEncodedFilename(UUID id);
    HeadObjectResponse getHead(UUID id);
    List<String> listFiles(String bucket);
    void deleteFile(UUID id);
    String getFileKey(UUID id);
}

