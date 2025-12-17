package dev.aurivena.lms.domain.minio;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioService minioService;

    @Test
    void init_BucketExists() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        // Act
        minioService.init();

        // Assert
        verify(minioClient, never()).makeBucket(any(MakeBucketArgs.class));
    }

    @Test
    void init_BucketDoesNotExist() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);

        // Act
        minioService.init();

        // Assert
        verify(minioClient).makeBucket(any(MakeBucketArgs.class));
    }

    @Test
    void upload_Success() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        // Act
        String result = minioService.upload(file);

        // Assert
        assertNotNull(result);
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void get_Success() throws Exception {
        // Arrange
        String filename = "test.txt";
        byte[] content = "content".getBytes();
        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(content);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(response);

        // Act
        byte[] result = minioService.get(filename);

        // Assert
        assertArrayEquals(content, result);
    }

    @Test
    void delete_Success() throws Exception {
        // Arrange
        String filename = "test.txt";

        // Act
        minioService.delete(filename);

        // Assert
        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }
}
