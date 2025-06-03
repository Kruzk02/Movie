package com.app.Service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class VideoStreamService {

    public static byte[] readByteRange(String filename, long start, long end) {
        try {
            Path path = Paths.get(Objects.requireNonNull(getFilePath()), filename);
            byte[] data = Files.readAllBytes(path);
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
            return result;
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static Long getFileSize(String fileName) {
        return Optional.ofNullable(fileName)
                .map(file -> Paths.get(Objects.requireNonNull(getFilePath()), file))
                .map(VideoStreamService::sizeFromFile)
                .orElse(0L);
    }

    private static Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            return 0L;
        }
    }

    private static String getFilePath() {
        try {
            Resource videoResource = new FileSystemResource("movieMedia/");
            return new File(String.valueOf(videoResource.getFile())).getAbsolutePath();
        }catch (IOException e) {
            return null;
        }
    }
}
