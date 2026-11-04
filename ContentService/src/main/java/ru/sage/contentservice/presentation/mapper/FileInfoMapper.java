package ru.sage.contentservice.presentation.mapper;

import lombok.experimental.UtilityClass;
import ru.sage.contentservice.domain.model.FileDomain;
import ru.sage.contentservice.presentation.model.FileInfoResponse;

import java.util.List;

@UtilityClass
public class FileInfoMapper {

    public static FileInfoResponse toPresentation(FileDomain file) {

        if (file == null) {
            return null;
        }

        return new FileInfoResponse(
                file.id(),
                file.noteId(),
                file.fid(),
                file.filename(),
                file.bucket(),
                file.key(),
                file.url(),
                file.extension(),
                file.mimeType(),
                file.size(),
                file.createdAt(),
                file.updatedAt()
        );
    }

    public static List<FileInfoResponse> toPresentation(List<FileDomain> files) {
        return files.stream().map(x -> new FileInfoResponse(
                x.id(),
                x.noteId(),
                x.fid(),
                x.filename(),
                x.bucket(),
                x.key(),
                x.url(),
                x.extension(),
                x.mimeType(),
                x.size(),
                x.createdAt(),
                x.updatedAt()
        )).toList();
    }
}
