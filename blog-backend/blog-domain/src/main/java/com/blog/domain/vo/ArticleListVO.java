package com.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleListVO {

    private Long id;
    private String title;
    private String summary;
    private String cover;
    private String authorName;
    private String categoryName;
    private String tags;
    private LocalDateTime createTime;
}
