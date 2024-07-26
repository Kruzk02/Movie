package com.app.Service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<Void> get(String filename,String folder);
    Mono<Void> save(FilePart filePart, String folder,String filename);
    Mono<Void> delete(String folder,String filename);
}
