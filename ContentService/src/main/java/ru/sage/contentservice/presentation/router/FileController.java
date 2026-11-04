package ru.sage.contentservice.presentation.router;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.sage.contentservice.domain.usecase.FileUseCase;
import ru.sage.contentservice.presentation.mapper.FileInfoMapper;
import ru.sage.contentservice.presentation.model.FileInfoResponse;
import ru.sage.contentservice.presentation.model.FileUploadRequest;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@ExtensionMethod(FileInfoMapper.class)
public class FileController {

    private final FileUseCase fileUseCase;

    @Autowired
    FileController(FileUseCase fileUseCase) {
        this.fileUseCase = fileUseCase;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @ModelAttribute FileUploadRequest fileUploadRequest) throws IOException {

        String key = fileUseCase.uploadFile(fileUploadRequest.noteId(), fileUploadRequest.userId(), fileUploadRequest.file());

        return ResponseEntity.ok("Загрузка успешна: " + key);
    }

    @GetMapping("/download/{file_id}")
    public ResponseEntity<StreamingResponseBody> download(
            @PathVariable UUID file_id) {

        StreamingResponseBody body = fileUseCase.downloadFile(file_id);
        HeadObjectResponse head = fileUseCase.getHead(file_id);
        String encodedFileName = fileUseCase.getEncodedFilename(file_id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(head.contentLength())
                .body(body);
    }

    @GetMapping("/presigned-url/{file_id}")
    public ResponseEntity<String> getPresignedUrl(
            @PathVariable UUID file_id) {

        return ResponseEntity.ok(fileUseCase.getPresignedUrl(file_id));
    }

    @GetMapping("/info")
    public ResponseEntity<List<FileInfoResponse>> getAllFilesInfo() {

        return ResponseEntity.ok(fileUseCase.getAllFilesInfo().toPresentation());
    }

    @GetMapping("/info/{file_id}")
    public ResponseEntity<FileInfoResponse> getFileInfoById(
            @PathVariable UUID file_id
    ) {
        return ResponseEntity.ok(fileUseCase.getFileInfoById(file_id).toPresentation());
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> list(
            @RequestParam String bucket) {

        return ResponseEntity.ok(fileUseCase.listFiles(bucket));
    }

    @DeleteMapping("/delete/{file_id}")
    public ResponseEntity<String> delete(
            @PathVariable UUID file_id) {

        fileUseCase.deleteFile(file_id);

        return ResponseEntity.ok("Удаление успешно");
    }
}
