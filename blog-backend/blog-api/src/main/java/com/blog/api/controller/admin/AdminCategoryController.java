package com.blog.api.controller.admin;

import com.blog.application.service.CategoryService;
import com.blog.common.result.Result;
import com.blog.domain.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "后台管理 - 分类", description = "后台分类管理接口（管理员专用）")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取分类列表", description = "获取所有分类列表")
    public Result<List<Category>> list() {
        return Result.success(categoryService.getAllCategories());
    }

    @PostMapping
    @Operation(summary = "创建分类", description = "管理员创建新分类")
    public Result<Category> create(
            @Parameter(description = "分类实体", required = true) @RequestBody Category category) {
        return Result.success(categoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "管理员更新分类信息")
    public Result<Category> update(
            @Parameter(description = "分类 ID", required = true) @PathVariable Long id,
            @Parameter(description = "分类实体", required = true) @RequestBody Category category) {
        return Result.success(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "管理员删除分类")
    public Result<Void> delete(@Parameter(description = "分类 ID", required = true) @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
