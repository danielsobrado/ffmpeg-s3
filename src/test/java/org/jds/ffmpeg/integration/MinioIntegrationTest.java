package org.jds.ffmpeg.integration;

import io.minio.MinioClient;
import org.jds.ffmpeg.service.MinioFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Tag("integration")
@Testcontainers
public class MinioIntegrationTest {

    private MinioFileService minioFileService;

    @Container
    public static GenericContainer<?> minioContainer = new GenericContainer<>(DockerImageName.parse("minio/minio"))
            .withExposedPorts(9000)
            .withEnv("MINIO_ROOT_USER", "minio")
            .withEnv("MINIO_ROOT_PASSWORD", "minio123")
            .withCommand("server", "/data")
            .waitingFor(Wait.forHttp("/minio/health/ready"));

    private String endpoint;
    private String accessKey;
    private String secretKey;

    @BeforeEach
    void setUp() {
        endpoint = "http://" + minioContainer.getHost() + ":" + minioContainer.getMappedPort(9000);
        accessKey = "minio";
        secretKey = "minio123";

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        minioFileService = new MinioFileService(minioClient);
    }

    @Test
    void testUploadAndDownloadFile() throws Exception {
        String bucketName = "test-bucket";
        String objectName = "test/object";
        byte[] testContent = "Test content".getBytes();

        try (InputStream inputStream = new ByteArrayInputStream(testContent)) {
            String contentType = "text/plain";

            minioFileService.createBucket(bucketName);

            minioFileService.uploadFile(bucketName, objectName, inputStream, contentType);
        }

        // Download the file
        try (InputStream resultStream = minioFileService.downloadFile(bucketName, objectName)) {
            byte[] resultContent = resultStream.readAllBytes();

            assertArrayEquals(testContent, resultContent);
        }
    }
}
