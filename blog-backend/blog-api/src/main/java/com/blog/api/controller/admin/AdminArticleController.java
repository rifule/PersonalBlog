package com.blog.api.controller.admin;

import com.blog.application.service.ArticleService;
import com.blog.common.result.Result;
import com.blog.domain.dto.ArticleDTO;
import com.blog.domain.vo.ArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
@Tag(name = "后台管理 - 文章", description = "后台文章管理接口（管理员专用）")
public class AdminArticleController {

    private final ArticleService articleService;

    @PostMapping
    @Operation(summary = "创建文章", description = "管理员创建新文章")
    public Result<ArticleVO> create(
            @Parameter(description = "文章 DTO", required = true) @Valid @RequestBody ArticleDTO articleDTO) {
        // 临时使用默认用户ID 1，后续可以从 SecurityContext 获取
        return Result.success(articleService.createArticle(articleDTO, 1L));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章", description = "管理员更新文章信息")
    public Result<ArticleVO> update(
            @Parameter(description = "文章 ID", required = true) @PathVariable Long id,
            @Parameter(description = "文章 DTO", required = true) @Valid @RequestBody ArticleDTO articleDTO) {
        return Result.success(articleService.updateArticle(id, articleDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章", description = "管理员删除文章")
    public Result<Void> delete(@Parameter(description = "文章 ID", required = true) @PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情", description = "管理员获取文章详情")
    public Result<ArticleVO> getById(@Parameter(description = "文章 ID", required = true) @PathVariable Long id) {
        return Result.success(articleService.getArticleById(id));
    }
}
