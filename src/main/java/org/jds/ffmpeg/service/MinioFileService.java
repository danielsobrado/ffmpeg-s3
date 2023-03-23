package org.jds.ffmpeg.service;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioFileService {

    @Autowired
    private MinioClient minioClient;

    public MinioFileService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) throws IOException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, -1, 10485760) // You may need to adjust the part size depending on your use case.
                        .contentType(contentType)
                        .build()
        );
    }

    public InputStream downloadFile(String bucketName, String objectName) throws IOException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public void createBucket(String bucketName) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, XmlParserException, InsufficientDataException, ErrorResponseException, InternalException {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}
