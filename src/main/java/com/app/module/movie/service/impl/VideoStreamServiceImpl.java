package com.app.module.movie.service.impl;

import com.app.module.movie.service.VideoStreamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@Service
public class VideoStreamServiceImpl implements VideoStreamService {

    private static final Logger log = LogManager.getLogger(VideoStreamServiceImpl.class);

    @Override
    public byte[] readByteRange(String filename, long start, long end) {
        try {
            Path path = Paths.get(Objects.requireNonNull(getFilePath()), filename);
            byte[] data = Files.readAllBytes(path);
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
            return result;
        } catch (IOException e) {
            log.error("Exception while reading the file: ", e);
            return new byte[0];
        }
    }

    @Override
    public Long getFileSize(String fileName) {
        return Optional.ofNullable(fileName)
                .map(file -> Paths.get(Objects.requireNonNull(getFilePath()), file))
                .map(this::sizeFromFile)
                .orElse(0L);
    }

    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            log.error("Error while getting the file size: ", e);
            return 0L;
        }
    }

    private String getFilePath() {
        try {
            Resource videoResource = new FileSystemResource("movieMedia/");
            return new File(String.valueOf(videoResource.getFile())).getAbsolutePath();
        }catch (IOException e) {
            log.error("Error while getting the file path: ", e);
            return null;
        }
    }
}
