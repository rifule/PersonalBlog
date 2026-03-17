package com.blog.application.service.impl;

import com.blog.application.service.UserService;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCode;
import com.blog.common.utils.JwtUtil;
import com.blog.domain.dto.LoginDTO;
import com.blog.domain.dto.UserDTO;
import com.blog.domain.entity.User;
import com.blog.domain.enums.UserRole;
import com.blog.domain.repository.UserRepository;
import com.blog.domain.vo.UserVO;
import com.blog.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.server.url:http://localhost:8080}")
    private String serverUrl;

    @Override
    public UserVO login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        
        // 如果用户不存在，自动创建默认管理员用户（仅用于开发测试）
        if (user == null && "admin".equals(loginDTO.getUsername())) {
            user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setEmail("admin@example.com");
            user.setAvatar("/image/sadrudy.jpg");
            user.setRole(UserRole.ADMIN);
            user.setStatus(1);
            userRepository.insert(user);
        }
        
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 临时使用明文密码验证（仅用于测试）
        // 如果密码匹配 BCrypt 加密后的密码，或者是明文密码 "admin123"
        boolean passwordMatches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())
                || "admin123".equals(loginDTO.getPassword());
        
        if (!passwordMatches) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        if (user.getStatus() != 1) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    @Transactional
    public UserVO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        if (userDTO.getNickname() != null) {
            user.setNickname(userDTO.getNickname());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getAvatar() != null) {
            user.setAvatar(userDTO.getAvatar());
        }
        if (userDTO.getGithubUrl() != null) {
            user.setGithubUrl(userDTO.getGithubUrl());
        }

        userRepository.updateById(user);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return getCurrentUser(userId);
    }

    @Override
    @Transactional
    public String uploadAvatar(MultipartFile file, Long userId) {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ResultCode.FILE_TYPE_ERROR);
        }

        // 检查文件大小（最大 5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException(ResultCode.FILE_SIZE_ERROR);
        }

        try {
            // 创建上传目录
            String uploadDir = System.getProperty("user.dir") + "/uploads/avatars/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            // 返回完整访问URL
            String avatarUrl = serverUrl + "/uploads/avatars/" + filename;

            // 更新用户头像（存储相对路径）
            updateAvatar("/uploads/avatars/" + filename, userId);

            return avatarUrl;
        } catch (IOException e) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateAvatar(String avatarUrl, Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        user.setAvatar(avatarUrl);
        userRepository.updateById(user);
    }
}
