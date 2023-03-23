package org.jds.ffmpeg.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import org.jds.ffmpeg.util.FfmpegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class FileConversionService {

    private static final Logger logger = LoggerFactory.getLogger(FileConversionService.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${tempDir}")
    private String tempDir;

    public boolean convertAndSaveFile(String inputBucket, String inputFileKey, String outputBucket, String outputFileKey, String inputFormat, String outputFormat) {
        String inputFilePath = tempDir + File.separator + "input." + inputFormat;

        try {
            // Download the input file from Minio
            try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(inputBucket)
                    .object(inputFileKey)
                    .build())) {
                Files.copy(inputStream, Path.of(inputFilePath), StandardCopyOption.REPLACE_EXISTING);
            }

            // Generate a pre-signed URL for uploading the output file to Minio
            String outputUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(outputBucket)
                            .object(outputFileKey)
                            .expiry(1, TimeUnit.HOURS)
                            .extraQueryParams(new HashMap<>())
                            .build()
            );

            // Convert the file using FFmpeg and write the output directly to the Minio bucket
            boolean conversionSuccess = FfmpegUtil.convertFileAndWriteToUrl(inputFilePath, outputUrl, inputFormat, outputFormat);
            if (!conversionSuccess) {
                return false;
            }

            // Clean up the temporary input file
            File inputFile = new File(inputFilePath);
            if (inputFile.delete()) {
                logger.info("Deleted temporary input file: {}", inputFilePath);
            } else {
                logger.warn("Failed to delete temporary input file: {}", inputFilePath);
            }

            return true;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            logger.error("Error during file conversion and saving: {}", e.getMessage(), e);
            return false;
        }
    }
}
