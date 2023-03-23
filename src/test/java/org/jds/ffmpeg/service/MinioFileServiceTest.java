package org.jds.ffmpeg.service;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MinioFileServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioFileService minioFileService;

    private String bucketName;
    private String objectName;

    @BeforeEach
    void setUp() {
        bucketName = "test-bucket";
        objectName = "test/object";
    }

    @Test
    void uploadFileSuccess() throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] testContent = "Test content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testContent);
        String contentType = "text/plain";

        minioFileService.uploadFile(bucketName, objectName, inputStream, contentType);

        verify(minioClient, times(1)).putObject(any());
    }

    @Test
    void downloadFileSuccess() throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
        InputStream inputStream = mock(InputStream.class);

        when(getObjectResponse.readAllBytes()).thenReturn("Test content".getBytes());
        when(minioClient.getObject(any())).thenReturn(getObjectResponse);

        InputStream result = minioFileService.downloadFile(bucketName, objectName);

        verify(minioClient, times(1)).getObject(any());
    }
}
