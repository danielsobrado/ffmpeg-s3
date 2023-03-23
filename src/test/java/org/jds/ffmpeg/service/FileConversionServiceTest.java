package org.jds.ffmpeg.service;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.PutObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.jds.ffmpeg.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileConversionServiceTest {

    @Mock
    private MinioClient minioClient;

    @Autowired
    private TestConfig testConfig;

    @InjectMocks
    private FileConversionService fileConversionService;

    private String inputBucket;
    private String inputFileKey;
    private String outputBucket;
    private String outputFileKey;
    private String inputFormat;
    private String outputFormat;

    @BeforeEach
    void setUp() {
        inputBucket = "test-input-bucket";
        inputFileKey = "test/input/file";
        outputBucket = "test-output-bucket";
        outputFileKey = "test/output/file";
        inputFormat = "avi";
        outputFormat = "mp4";
    }

    @Test
    void convertAndSaveFileSuccess() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(getObjectResponse);

        boolean result = fileConversionService.convertAndSaveFile(inputBucket, inputFileKey, outputBucket, outputFileKey, inputFormat, outputFormat);
        assertTrue(result);

        verify(minioClient, times(1)).getObject(GetObjectArgs.builder().bucket(inputBucket).object(inputFileKey).build());
    }

    @Test
    void convertAndSaveFileFailure() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        when(minioClient.getObject(any(GetObjectArgs.class))).thenThrow(IOException.class);

        boolean result = fileConversionService.convertAndSaveFile(inputBucket, inputFileKey, outputBucket, outputFileKey, inputFormat, outputFormat);
        assertFalse(result);

        verify(minioClient, times(1)).getObject(GetObjectArgs.builder().bucket(inputBucket).object(inputFileKey).build());
    }
}
