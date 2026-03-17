package com.blog.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository extends BaseMapper<User> {

    @Select("SELECT * FROM tb_user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM tb_user WHERE username = #{username}")
    java.util.Optional<User> findOptionalByUsername(String username);

    @Select("SELECT COUNT(*) FROM tb_user WHERE username = #{username}")
    boolean existsByUsername(String username);

    @Select("SELECT COUNT(*) FROM tb_user WHERE email = #{email}")
    boolean existsByEmail(String email);
}
