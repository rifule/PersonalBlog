package com.blog.api.controller.admin;

import com.blog.application.service.FileService;
import com.blog.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
@Tag(name = "后台管理 - 文件上传", description = "文件上传接口（管理员专用）")
public class AdminFileController {

    private final FileService fileService;

    @Value("${file.upload.url:/uploads/}")
    private String fileUrlPrefix;

    @PostMapping("/image")
    @Operation(summary = "上传图片", description = "上传图片文件")
    public Result<Map<String, String>> uploadImage(
            @Parameter(description = "图片文件", required = true) @RequestParam("file") MultipartFile file) {
        String fileName = fileService.uploadImage(file);
        String url = fileUrlPrefix + fileName;

        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        result.put("name", fileName);

        return Result.success(result);
    }

    @PostMapping("/markdown")
    @Operation(summary = "上传 Markdown 图片", description = "在 Markdown 编辑器中粘贴上传图片")
    public Result<Map<String, String>> uploadMarkdownImage(
            @Parameter(description = "图片文件", required = true) @RequestParam("file") MultipartFile file) {
        String fileName = fileService.uploadMarkdownImage(file);
        String url = fileUrlPrefix + fileName;

        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        result.put("name", fileName);

        return Result.success(result);
    }
}
