package com.app.Service.Impl;

import com.app.Service.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LogManager.getLogger(FileServiceImpl.class);

    private File ensureFolderExist(String folder) {
        File directory = new File(folder);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                log.info("Created directory: {}", folder);
            } else {
                log.error("Failed to create directory: {}", folder);
            }
        }
        return directory;
    }

    @Override
    public Mono<Void> get(String filename, String folder) {
        Path filePath = Paths.get(folder, filename);
        File file = filePath.toFile();
        if (file.exists()) {
            Resource resource = new FileSystemResource(file);
            log.info(file.toString());
            return Mono.just(resource).then();
        } else {
            return Mono.error(new FileNotFoundException("File not found: " + filename));
        }
    }

    @Override
    public Mono<Void> save(FilePart filePart, String folder, String filename) {
        File directory = ensureFolderExist(folder);
        File file = new File(directory, filename);
        log.info("Saving file to: {}", file.getAbsolutePath());
        return filePart.transferTo(file)
                .doOnSuccess(unused -> log.info("File saved successfully: {}", filename))
                .doOnError(error -> log.error("Failed to save file: {}", filename, error));
    }

    @Override
    public Mono<Void> delete(String folder,String filename) {
        Path path = Paths.get(folder, filename);
        File file = path.toFile();
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.info("File deleted successfully.");
            }else {
                log.info("Failed to delete the file.");
            }
        }else {
            log.info("File does not exist.");
        }
        return Mono.empty();
    }
}
