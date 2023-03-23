package org.jds.ffmpeg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:test.properties")
public class TestConfig {

    @Autowired
    private Environment env;

    public String getMinioEndpoint() {
        return env.getProperty("minio.endpoint");
    }

    public String getMinioAccessKey() {
        return env.getProperty("minio.accessKey");
    }

    public String getMinioSecretKey() {
        return env.getProperty("minio.secretKey");
    }

    public String getFfmpegExecutablePath() {
        return env.getProperty("ffmpeg.executablePath");
    }

    public String getTempDir() {
        return env.getProperty("tempDir");
    }
}
