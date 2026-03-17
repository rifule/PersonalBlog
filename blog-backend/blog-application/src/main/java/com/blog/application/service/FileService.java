package com.blog.application.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadImage(MultipartFile file);

    String uploadMarkdownImage(MultipartFile file);

    void deleteFile(String fileName);
}
