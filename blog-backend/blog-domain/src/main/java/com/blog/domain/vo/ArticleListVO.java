package com.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleListVO {

    private Long id;
    private String title;
    private String summary;
    private String cover;
    private String authorName;
    private String categoryName;
    private List<Long> tagIds;
    private List<String> tags;
    private LocalDateTime createTime;
}
