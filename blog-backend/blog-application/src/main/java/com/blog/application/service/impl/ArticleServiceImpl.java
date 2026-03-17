package com.blog.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.application.service.ArticleService;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCode;
import com.blog.common.utils.PageResult;
import com.blog.domain.dto.ArticleDTO;
import com.blog.domain.entity.Article;
import com.blog.domain.entity.Category;
import com.blog.domain.entity.User;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.CategoryRepository;
import com.blog.domain.repository.UserRepository;
import com.blog.domain.vo.ArticleListVO;
import com.blog.domain.vo.ArticleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ArticleVO createArticle(ArticleDTO articleDTO, Long authorId) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setAuthorId(authorId);

        articleRepository.insert(article);

        return convertToVO(article);
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

        return convertToVO(article);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
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
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Article::getTags, tagName);
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
