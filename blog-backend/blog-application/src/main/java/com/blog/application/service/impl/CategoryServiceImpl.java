package com.blog.application.service.impl;

import com.blog.application.service.CategoryService;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCode;
import com.blog.domain.entity.Category;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        categoryRepository.insert(category);
        return category;
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepository.selectById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.ERROR);
        }

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setSortOrder(category.getSortOrder());

        categoryRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderBySortOrderAsc();
        // 统计每个分类的文章数量
        for (Category category : categories) {
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.blog.domain.entity.Article> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            wrapper.eq(com.blog.domain.entity.Article::getCategoryId, category.getId());
            long count = articleRepository.selectCount(wrapper);
            category.setArticleCount((int) count);
        }
        return categories;
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.selectById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.ERROR);
        }
        return category;
    }
}
