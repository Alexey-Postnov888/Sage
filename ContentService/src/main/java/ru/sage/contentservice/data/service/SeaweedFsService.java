package ru.sage.contentservice.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeaweedFsService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Autowired
    public SeaweedFsService(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public void upload(String bucket, String key, MultipartFile file) throws IOException {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(request,
                    RequestBody.fromInputStream(inputStream, file.getSize()));
        }
    }

    public StreamingResponseBody download(String bucket, String key) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        StreamingResponseBody body = outputStream -> {
            try (InputStream inputStream = s3Client.getObject(request)) {
                inputStream.transferTo(outputStream);
            }
        };

        return body;
    }

    public String getPresignedUrl(String bucket, String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    public HeadObjectResponse getHead(String bucket, String key) {

        return s3Client.headObject(
                HeadObjectRequest.builder().bucket(bucket).key(key).build());
    }

    public List<String> list(String bucket) {

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix("")
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        List<String> keys = response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());

        return keys;
    }

    public void delete(String bucket, String key) {

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }

    public boolean bucketExists(String bucketName) {

        try {
            HeadBucketRequest request = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(request);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        } catch (S3Exception e) {
            System.err.println("Ошибка проверки: " + e.statusCode());
            return false;
        }
    }

    public void createBucket(String bucketName) {
        if (bucketExists(bucketName)) {
            return;
        }

        try {
            CreateBucketRequest request = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(request);
        } catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException e) {
            System.out.println("Bucket уже существует.");
        } catch (S3Exception e) {
            System.err.println("Ошибка создания: " + e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
