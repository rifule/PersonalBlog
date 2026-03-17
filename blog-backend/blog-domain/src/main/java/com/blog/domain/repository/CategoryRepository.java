package com.blog.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryRepository extends BaseMapper<Category> {

    @Select("SELECT * FROM tb_category ORDER BY sort_order ASC")
    List<Category> findAllByOrderBySortOrderAsc();
}
