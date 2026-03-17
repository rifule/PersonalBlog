package com.blog.api.controller;

import com.blog.application.service.ArticleService;
import com.blog.application.service.TagService;
import com.blog.common.result.Result;
import com.blog.common.utils.PageResult;
import com.blog.domain.entity.Tag;
import com.blog.domain.vo.ArticleListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理", description = "文章标签相关接口")
public class TagController {

    private final TagService tagService;
    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "获取标签列表", description = "获取所有文章标签列表")
    public Result<List<Tag>> list() {
        return Result.success(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取标签详情", description = "根据ID获取标签详情")
    public Result<Tag> getById(@Parameter(description = "标签ID") @PathVariable Long id) {
        return Result.success(tagService.getTagById(id));
    }

    @GetMapping("/{id}/articles")
    @Operation(summary = "获取标签下的文章", description = "根据标签ID获取该标签下的文章列表")
    public Result<PageResult<ArticleListVO>> getArticlesByTag(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        Tag tag = tagService.getTagById(id);
        return Result.success(articleService.getArticlesByTag(tag.getName(), pageNum, pageSize));
    }

    @PostMapping
    @Operation(summary = "创建标签", description = "创建新的文章标签")
    public Result<Tag> create(
            @Parameter(description = "标签信息") @Valid @RequestBody Tag tag) {
        return Result.success(tagService.createTag(tag));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新标签", description = "更新标签信息")
    public Result<Tag> update(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @Parameter(description = "标签信息") @Valid @RequestBody Tag tag) {
        return Result.success(tagService.updateTag(id, tag));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签", description = "删除指定标签")
    public Result<Void> delete(@Parameter(description = "标签ID") @PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
}
