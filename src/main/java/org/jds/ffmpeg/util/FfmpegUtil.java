package org.jds.ffmpeg.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FfmpegUtil {

    public static boolean convertFileAndWriteToUrl(String inputFile, String outputUrl, String inputFormat, String outputFormat) {
        String command = String.format("ffmpeg -i %s -f %s -method PUT %s", inputFile, outputFormat, outputUrl);
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during file conversion: " + e.getMessage());
            return false;
        }
    }
}
