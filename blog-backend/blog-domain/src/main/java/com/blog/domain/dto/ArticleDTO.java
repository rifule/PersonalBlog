package com.blog.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ArticleDTO {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200")
    private String title;

    private String content;

    @Size(max = 500, message = "摘要长度不能超过500")
    private String summary;

    private String cover;

    private Long categoryId;

    private List<Long> tagIds;

    private Boolean isTop;
}
