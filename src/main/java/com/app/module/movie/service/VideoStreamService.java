package com.app.module.movie.service;


public interface VideoStreamService {
    byte[] readByteRange(String filename, long start, long end);
    Long getFileSize(String fileName);
}
