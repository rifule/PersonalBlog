package com.blog.api.controller;

import com.blog.application.service.ArticleService;
import com.blog.common.result.Result;
import com.blog.common.utils.PageResult;
import com.blog.domain.dto.ArticleDTO;
import com.blog.domain.vo.ArticleListVO;
import com.blog.domain.vo.ArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "文章管理", description = "文章列表、详情、搜索相关接口")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "获取文章列表", description = "分页获取已发布文章列表")
    public Result<PageResult<ArticleListVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(articleService.getArticleList(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情", description = "根据 ID 获取文章详情（前台接口）")
    public Result<ArticleVO> getById(@Parameter(description = "文章 ID") @PathVariable Long id) {
        return Result.success(articleService.getArticleByIdForFront(id));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "获取分类下的文章列表", description = "根据分类 ID 获取该分类下的文章列表")
    public Result<PageResult<ArticleListVO>> getByCategory(
            @Parameter(description = "分类 ID") @PathVariable Long categoryId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(articleService.getArticleListByCategory(categoryId, pageNum, pageSize));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索文章", description = "根据关键词搜索文章")
    public Result<PageResult<ArticleListVO>> search(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(articleService.searchArticles(keyword, pageNum, pageSize));
    }

    @PostMapping
    @Operation(summary = "创建文章", description = "创建新文章")
    public Result<ArticleVO> create(
            @Parameter(description = "文章信息") @Valid @RequestBody ArticleDTO articleDTO) {
        // 临时使用默认用户ID 1，后续可以从 SecurityContext 获取
        return Result.success(articleService.createArticle(articleDTO, 1L));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章", description = "更新文章信息")
    public Result<ArticleVO> update(
            @Parameter(description = "文章ID") @PathVariable Long id,
            @Parameter(description = "文章信息") @Valid @RequestBody ArticleDTO articleDTO) {
        return Result.success(articleService.updateArticle(id, articleDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章", description = "删除指定文章")
    public Result<Void> delete(@Parameter(description = "文章ID") @PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    @GetMapping("/calendar")
    @Operation(summary = "获取文章日历", description = "获取指定年月有文章的日期列表")
    public Result<List<String>> getCalendar(
            @Parameter(description = "年份") @RequestParam int year,
            @Parameter(description = "月份") @RequestParam int month) {
        return Result.success(articleService.getArticleCalendar(year, month));
    }
}
