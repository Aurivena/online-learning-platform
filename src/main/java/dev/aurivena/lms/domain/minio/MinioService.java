package dev.aurivena.lms.domain.minio;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioService {
    private MinioClient minioClient;
    private String bucketName = "lms-bucket";

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }


    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            } else {
                throw new RuntimeException("Bucket {} already exists bucketName");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации MinIO / создания бакета", e);
        }
    }

    public String upload(MultipartFile file) {
        String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            String es = e.getMessage();
            String ec = String.valueOf(e.getClass());
            throw new RuntimeException("Ошибки загрузки файла в minio", e);
        }

        return fileName;
    }

    public byte[] get(String filename) {
        try {
            InputStream is = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );

            return is.readAllBytes();
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                return null;
            }
            throw new RuntimeException("Ошибка при чтении файла из MinIO", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла из MinIO", e);
        }
    }
}
