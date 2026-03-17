package com.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleVO {

    private Long id;
    private String title;
    private String content;
    private String summary;
    private String cover;
    private Long authorId;
    private String authorName;
    private Long categoryId;
    private String categoryName;
    private String tags;
    private Boolean isTop;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
