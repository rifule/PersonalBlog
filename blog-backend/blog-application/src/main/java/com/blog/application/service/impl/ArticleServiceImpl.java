package com.blog.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.application.service.ArticleService;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCode;
import com.blog.common.utils.PageResult;
import com.blog.domain.dto.ArticleDTO;
import com.blog.domain.entity.Article;
import com.blog.domain.entity.ArticleTag;
import com.blog.domain.entity.Category;
import com.blog.domain.entity.Tag;
import com.blog.domain.entity.User;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.ArticleTagRepository;
import com.blog.domain.repository.CategoryRepository;
import com.blog.domain.repository.TagRepository;
import com.blog.domain.repository.UserRepository;
import com.blog.domain.vo.ArticleListVO;
import com.blog.domain.vo.ArticleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleTagRepository articleTagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public ArticleVO createArticle(ArticleDTO articleDTO, Long authorId) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setAuthorId(authorId);

        articleRepository.insert(article);

        // 保存文章标签关联
        saveArticleTags(article.getId(), articleDTO.getTagIds());

        return convertToVO(article);
    }

    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (articleId == null) {
            return;
        }
        // 删除旧的关联
        articleTagRepository.deleteByArticleId(articleId);
        // 如果标签为空，直接返回
        if (CollectionUtils.isEmpty(tagIds)) {
            return;
        }
        // 插入新的关联
        for (Long tagId : tagIds) {
            if (tagId != null) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tagId);
                articleTagRepository.insert(articleTag);
            }
        }
    }

    @Override
    @Transactional
    public ArticleVO updateArticle(Long id, ArticleDTO articleDTO) {
        Article article = articleRepository.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_EXIST);
        }

        BeanUtils.copyProperties(articleDTO, article);
        articleRepository.updateById(article);

        // 更新文章标签关联
        saveArticleTags(id, articleDTO.getTagIds());

        return convertToVO(article);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
        // 删除文章标签关联
        articleTagRepository.deleteByArticleId(id);
    }

    @Override
    public ArticleVO getArticleById(Long id) {
        Article article = articleRepository.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_EXIST);
        }
        return convertToVO(article);
    }

    @Override
    @Transactional
    public ArticleVO getArticleByIdForFront(Long id) {
        Article article = articleRepository.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_EXIST);
        }

        return convertToVO(article);
    }

    @Override
    public PageResult<ArticleListVO> getArticleList(int pageNum, int pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getIsTop);
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> resultPage = articleRepository.selectPage(page, wrapper);

        List<ArticleListVO> list = resultPage.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return new PageResult<>(resultPage.getTotal(), list, pageNum, pageSize);
    }

    @Override
    public PageResult<ArticleListVO> getArticleListByCategory(Long categoryId, int pageNum, int pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getCategoryId, categoryId);
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> resultPage = articleRepository.selectPage(page, wrapper);

        List<ArticleListVO> list = resultPage.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return new PageResult<>(resultPage.getTotal(), list, pageNum, pageSize);
    }

    @Override
    public PageResult<ArticleListVO> searchArticles(String keyword, int pageNum, int pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(Article::getTitle, keyword)
                .or()
                .like(Article::getSummary, keyword)
                .or()
                .like(Article::getContent, keyword));
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> resultPage = articleRepository.selectPage(page, wrapper);

        List<ArticleListVO> list = resultPage.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return new PageResult<>(resultPage.getTotal(), list, pageNum, pageSize);
    }

    @Override
    public PageResult<ArticleListVO> getArticlesByTag(String tagName, int pageNum, int pageSize) {
        // 先根据标签名查询标签ID
        LambdaQueryWrapper<Tag> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.eq(Tag::getName, tagName);
        Tag tag = tagRepository.selectOne(tagWrapper);

        if (tag == null) {
            return new PageResult<>(0L, Collections.emptyList(), pageNum, pageSize);
        }

        // 查询关联的文章ID列表
        List<Long> articleIds = articleTagRepository.selectArticleIdsByTagId(tag.getId());

        if (CollectionUtils.isEmpty(articleIds)) {
            return new PageResult<>(0L, Collections.emptyList(), pageNum, pageSize);
        }

        // 分页查询文章
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Article::getId, articleIds);
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> resultPage = articleRepository.selectPage(page, wrapper);

        List<ArticleListVO> list = resultPage.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return new PageResult<>(resultPage.getTotal(), list, pageNum, pageSize);
    }

    private ArticleVO convertToVO(Article article) {
        ArticleVO vo = new ArticleVO();
        BeanUtils.copyProperties(article, vo);

        User user = userRepository.selectById(article.getAuthorId());
        if (user != null) {
            vo.setAuthorName(user.getUsername());
        }

        if (article.getCategoryId() != null) {
            Category category = categoryRepository.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        // 查询文章标签
        List<Long> tagIds = articleTagRepository.selectTagIdsByArticleId(article.getId());
        vo.setTagIds(tagIds);
        if (!CollectionUtils.isEmpty(tagIds)) {
            List<String> tagNames = tagIds.stream()
                    .map(tagId -> {
                        Tag tag = tagRepository.selectById(tagId);
                        return tag != null ? tag.getName() : null;
                    })
                    .filter(name -> name != null)
                    .collect(Collectors.toList());
            vo.setTags(tagNames);
        } else {
            vo.setTags(Collections.emptyList());
        }

        return vo;
    }

    private ArticleListVO convertToListVO(Article article) {
        ArticleListVO vo = new ArticleListVO();
        BeanUtils.copyProperties(article, vo);

        User user = userRepository.selectById(article.getAuthorId());
        if (user != null) {
            vo.setAuthorName(user.getUsername());
        }

        if (article.getCategoryId() != null) {
            Category category = categoryRepository.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        // 查询文章标签
        List<Long> tagIds = articleTagRepository.selectTagIdsByArticleId(article.getId());
        vo.setTagIds(tagIds);
        if (!CollectionUtils.isEmpty(tagIds)) {
            List<String> tagNames = tagIds.stream()
                    .map(tagId -> {
                        Tag tag = tagRepository.selectById(tagId);
                        return tag != null ? tag.getName() : null;
                    })
                    .filter(name -> name != null)
                    .collect(Collectors.toList());
            vo.setTags(tagNames);
        } else {
            vo.setTags(Collections.emptyList());
        }

        return vo;
    }

    @Override
    public List<String> getArticleCalendar(int year, int month) {
        // 构建月份的开始和结束时间
        String startDate = String.format("%d-%02d-01", year, month);
        String endDate = String.format("%d-%02d-31", year, month);

        // 查询该月份有文章的日期
        return articleRepository.selectArticleDatesByMonth(startDate, endDate);
    }
}
