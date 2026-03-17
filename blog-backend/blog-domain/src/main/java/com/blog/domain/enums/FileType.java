package com.blog.domain.enums;

import lombok.Getter;

@Getter
public enum FileType {
    IMAGE("IMAGE", "图片"),
    DOCUMENT("DOCUMENT", "文档"),
    CODE("CODE", "代码"),
    DATA("DATA", "数据"),
    OTHER("OTHER", "其他");

    private final String code;
    private final String description;

    FileType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
