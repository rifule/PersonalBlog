package com.blog.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleRepository extends BaseMapper<com.blog.domain.entity.Article> {

    @Select("SELECT DISTINCT DATE_FORMAT(create_time, '%Y-%m-%d') as article_date " +
            "FROM tb_article " +
            "WHERE DATE_FORMAT(create_time, '%Y-%m-%d') BETWEEN #{startDate} AND #{endDate} " +
            "ORDER BY article_date")
    List<String> selectArticleDatesByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
