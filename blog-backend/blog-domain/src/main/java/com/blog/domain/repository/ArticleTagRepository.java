package com.blog.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleTagRepository extends BaseMapper<ArticleTag> {

    /**
     * 根据文章ID查询标签ID列表
     */
    @Select("SELECT tag_id FROM tb_article_tag WHERE article_id = #{articleId} AND deleted = 0")
    List<Long> selectTagIdsByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据标签ID查询文章ID列表
     */
    @Select("SELECT article_id FROM tb_article_tag WHERE tag_id = #{tagId} AND deleted = 0")
    List<Long> selectArticleIdsByTagId(@Param("tagId") Long tagId);

    /**
     * 批量插入文章标签关联
     */
    int batchInsert(@Param("articleId") Long articleId, @Param("tagIds") List<Long> tagIds);

    /**
     * 根据文章ID删除所有标签关联
     */
    int deleteByArticleId(@Param("articleId") Long articleId);
}
