package com.blog.application.service;

import com.blog.common.utils.PageResult;
import com.blog.domain.dto.ArticleDTO;
import com.blog.domain.vo.ArticleListVO;
import com.blog.domain.vo.ArticleVO;

import java.util.List;

public interface ArticleService {

    ArticleVO createArticle(ArticleDTO articleDTO, Long authorId);

    ArticleVO updateArticle(Long id, ArticleDTO articleDTO);

    void deleteArticle(Long id);

    ArticleVO getArticleById(Long id);

    ArticleVO getArticleByIdForFront(Long id);

    PageResult<ArticleListVO> getArticleList(int pageNum, int pageSize);

    PageResult<ArticleListVO> getArticleListByCategory(Long categoryId, int pageNum, int pageSize);

    PageResult<ArticleListVO> searchArticles(String keyword, int pageNum, int pageSize);

    PageResult<ArticleListVO> getArticlesByTag(String tagName, int pageNum, int pageSize);

    List<String> getArticleCalendar(int year, int month);
}
