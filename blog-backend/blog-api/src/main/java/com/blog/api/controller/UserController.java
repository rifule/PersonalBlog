package com.blog.api.controller;

import com.blog.application.service.UserService;
import com.blog.common.result.Result;
import com.blog.common.result.ResultCode;
import com.blog.common.utils.JwtUtil;
import com.blog.domain.dto.UserDTO;
import com.blog.domain.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<UserVO> getCurrentUser(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        return Result.success(userService.getCurrentUser(userId));
    }

    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authHeader.substring(7);
            return JwtUtil.getUserId(token);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/avatar")
    @Operation(summary = "上传头像", description = "上传用户头像并更新用户信息")
    public Result<String> uploadAvatar(
            @Parameter(description = "头像文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        return Result.success(userService.uploadAvatar(file, userId));
    }

    @PutMapping("/avatar")
    @Operation(summary = "更新头像URL", description = "直接更新用户头像URL")
    public Result<Void> updateAvatar(
            @Parameter(description = "头像URL") @RequestParam("avatarUrl") String avatarUrl,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        userService.updateAvatar(avatarUrl, userId);
        return Result.success();
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息", description = "更新用户昵称、邮箱、简介等信息")
    public Result<UserVO> updateUserInfo(
            @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        return Result.success(userService.updateUser(userId, userDTO));
    }
}
