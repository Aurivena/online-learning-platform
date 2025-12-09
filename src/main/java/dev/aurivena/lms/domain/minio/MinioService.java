package dev.aurivena.lms.domain.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
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
