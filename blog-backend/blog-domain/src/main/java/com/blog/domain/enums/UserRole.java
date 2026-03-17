package com.blog.domain.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN", "管理员");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
