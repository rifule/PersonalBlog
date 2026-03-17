package com.blog.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagRepository extends BaseMapper<Tag> {

    @Select("SELECT * FROM tb_tag ORDER BY create_time DESC")
    List<Tag> findAllOrderByCreateTimeDesc();
}
