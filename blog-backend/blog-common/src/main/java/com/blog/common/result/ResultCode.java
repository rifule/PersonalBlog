package com.blog.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    
    // 认证相关
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有操作权限"),
    
    // 参数相关
    PARAM_ERROR(400, "参数错误"),
    PARAM_NULL(4001, "参数为空"),
    
    // 用户相关
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_ALREADY_EXIST(1003, "用户已存在"),
    
    // 文章相关
    ARTICLE_NOT_EXIST(2001, "文章不存在"),
    ARTICLE_ALREADY_EXIST(2002, "文章已存在"),
    
    // 文件相关
    FILE_UPLOAD_ERROR(3001, "文件上传失败"),
    FILE_NOT_EXIST(3002, "文件不存在"),
    FILE_TYPE_ERROR(3003, "文件类型不支持"),
    FILE_SIZE_ERROR(3004, "文件大小超过限制"),
    FILE_DELETE_ERROR(3005, "文件删除失败"),

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
