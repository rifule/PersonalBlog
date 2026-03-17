package com.blog.domain.vo;

import com.blog.domain.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private String githubUrl;
    private UserRole role;
    private Integer status;
    private LocalDateTime createTime;
}
