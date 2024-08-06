package com.app.Service;


public interface VideoStreamService {
    byte[] readByteRange(String filename, long start, long end);
    Long getFileSize(String fileName);
}
