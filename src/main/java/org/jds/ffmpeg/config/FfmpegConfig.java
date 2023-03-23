package org.jds.ffmpeg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FfmpegConfig {

    @Value("${ffmpeg.executablePath}")
    private String executablePath;

    @Bean
    public String ffmpegExecutablePath() {
        return executablePath;
    }
}
