package com.blog.api.controller;

import com.blog.application.service.CategoryService;
import com.blog.common.result.Result;
import com.blog.domain.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "文章分类相关接口")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取分类列表", description = "获取所有文章分类列表")
    public Result<List<Category>> list() {
        return Result.success(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详情")
    public Result<Category> getById(@Parameter(description = "分类ID") @PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "创建分类", description = "创建新的文章分类")
    public Result<Category> create(
            @Parameter(description = "分类信息") @Valid @RequestBody Category category) {
        return Result.success(categoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "更新分类信息")
    public Result<Category> update(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "分类信息") @Valid @RequestBody Category category) {
        return Result.success(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "删除指定分类")
    public Result<Void> delete(@Parameter(description = "分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
