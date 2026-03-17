package com.blog.application.service.impl;

import com.blog.application.service.FileService;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload.path:uploads/}")
    private String uploadPath;

    @Override
    public String uploadImage(MultipartFile file) {
        return uploadFile(file, "images/");
    }

    @Override
    public String uploadMarkdownImage(MultipartFile file) {
        return uploadFile(file, "markdown/");
    }

    private String uploadFile(MultipartFile file, String subDirectory) {
        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = getExtension(originalFilename);
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

            // 构建上传目录
            Path uploadDir = Paths.get(uploadPath, subDirectory);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 保存文件
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 返回相对路径
            return subDirectory + fileName;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadPath, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("文件删除失败", e);
            throw new BusinessException(ResultCode.FILE_DELETE_ERROR);
        }
    }

    private String getExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
