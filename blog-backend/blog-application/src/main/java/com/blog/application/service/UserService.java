package com.blog.application.service;

import com.blog.domain.dto.LoginDTO;
import com.blog.domain.dto.UserDTO;
import com.blog.domain.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserVO login(LoginDTO loginDTO);

    UserVO getCurrentUser(Long userId);

    UserVO getCurrentUser();

    UserVO updateUser(Long userId, UserDTO userDTO);

    String uploadAvatar(MultipartFile file, Long userId);

    void updateAvatar(String avatarUrl, Long userId);
}
