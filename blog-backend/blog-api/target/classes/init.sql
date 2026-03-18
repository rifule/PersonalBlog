-- 博客系统数据库初始化脚本
-- 一键执行即可生成所有表和初始数据

-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE blog_db;

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS tb_user;
CREATE TABLE tb_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名（登录用）',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    nickname VARCHAR(50) COMMENT '昵称（展示用）',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像URL',
    github_url VARCHAR(255) COMMENT 'GitHub链接',
    role VARCHAR(20) NOT NULL COMMENT '用户角色: ADMIN',
    status INT NOT NULL DEFAULT 1 COMMENT '状态: 1=启用, 0=禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 分类表
-- ============================================
DROP TABLE IF EXISTS tb_category;
CREATE TABLE tb_category (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- ============================================
-- 3. 标签表
-- ============================================
DROP TABLE IF EXISTS tb_tag;
CREATE TABLE tb_tag (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    name VARCHAR(50) NOT NULL COMMENT '标签名称',
    color VARCHAR(20) DEFAULT '#61dafb' COMMENT '标签颜色',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ============================================
-- 4. 文章表
-- ============================================
DROP TABLE IF EXISTS tb_article;
CREATE TABLE tb_article (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    title VARCHAR(200) NOT NULL COMMENT '文章标题',
    content TEXT NOT NULL COMMENT '文章内容（Markdown格式）',
    summary VARCHAR(500) COMMENT '文章摘要',
    cover VARCHAR(255) COMMENT '封面图URL',
    author_id BIGINT NOT NULL COMMENT '作者ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    is_top TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶: 0=否, 1=是',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    PRIMARY KEY (id),
    KEY idx_author_id (author_id),
    KEY idx_category_id (category_id),
    KEY idx_create_time (create_time),
    FULLTEXT KEY ft_title_content (title, content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- ============================================
-- 4.1 文章标签关联表
-- ============================================
DROP TABLE IF EXISTS tb_article_tag;
CREATE TABLE tb_article_tag (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    article_id BIGINT NOT NULL COMMENT '文章ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_article_tag (article_id, tag_id),
    KEY idx_article_id (article_id),
    KEY idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签关联表';

-- ============================================
-- 5. 评论表
-- ============================================
DROP TABLE IF EXISTS tb_comment;
CREATE TABLE tb_comment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    article_id BIGINT NOT NULL COMMENT '文章ID',
    parent_id BIGINT DEFAULT NULL COMMENT '父评论ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    status INT NOT NULL DEFAULT 1 COMMENT '状态: 1=审核通过, 0=待审核, -1=已拒绝',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    PRIMARY KEY (id),
    KEY idx_article_id (article_id),
    KEY idx_parent_id (parent_id),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================
-- 6. AI文件表
-- ============================================
DROP TABLE IF EXISTS tb_ai_file;
CREATE TABLE tb_ai_file (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件存储名',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_url VARCHAR(500) NOT NULL COMMENT '文件访问URL',
    file_type VARCHAR(50) NOT NULL COMMENT '文件类型: IMAGE/DOCUMENT/CODE/DATA/OTHER',
    file_size BIGINT COMMENT '文件大小（字节）',
    file_extension VARCHAR(20) COMMENT '文件扩展名',
    content_type VARCHAR(20) NOT NULL COMMENT '内容类型: IMAGE/DOCUMENT/CODE/DATA/OTHER',
    main_topic VARCHAR(200) COMMENT '主题',
    sub_topics VARCHAR(500) COMMENT '子主题（JSON）',
    summary TEXT COMMENT 'AI生成摘要',
    tags VARCHAR(200) COMMENT 'AI生成标签',
    confidence DOUBLE COMMENT '分类置信度',
    category_id BIGINT COMMENT '分类ID',
    related_article_id BIGINT COMMENT '关联文章ID',
    project_name VARCHAR(100) COMMENT '项目名',
    bucket_name VARCHAR(50) COMMENT 'MinIO桶名',
    storage_path VARCHAR(255) COMMENT '存储路径',
    upload_by BIGINT NOT NULL COMMENT '上传用户ID',
    is_public TINYINT NOT NULL DEFAULT 0 COMMENT '是否公开: 0=私有, 1=公开',
    metadata TEXT COMMENT '元数据（JSON）',
    ocr_text TEXT COMMENT 'OCR识别文本',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    PRIMARY KEY (id),
    KEY idx_file_type (file_type),
    KEY idx_upload_by (upload_by),
    KEY idx_category_id (category_id),
    KEY idx_related_article_id (related_article_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI文件表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 插入默认管理员用户（唯一用户）
-- 密码: admin123 (BCrypt加密)
INSERT INTO tb_user (username, password, nickname, email, avatar, github_url, role, status) 
VALUES ('admin', '$2a$10$N.MnnNFS4Z3vL2x2L2x2L2x2L2x2L2x2L2x2L2x2L2x2L2x2L2x2L2', '管理员', '1437178240@qq.com', '/image/sadrudy.jpg', 'https://github.com', 'ADMIN', 1);

-- 插入默认分类
INSERT INTO tb_category (name, description, sort_order) VALUES 
('技术', '技术相关文章，包括编程语言、框架、工具等', 1),
('生活', '生活随笔、日常点滴', 2),
('随想', '个人思考、感悟和想法', 3),
('其他', '其他未分类内容', 4);

-- 插入默认标签
INSERT INTO tb_tag (name, color) VALUES 
('Java', '#3b7a57'),
('Spring', '#6db33f'),
('Vue', '#42b883'),
('MySQL', '#00758f'),
('Redis', '#dc382f'),
('Docker', '#2496ed'),
('AI', '#ff6b6b'),
('前端', '#61dafb'),
('后端', '#f15bb5'),
('数据库', '#00bbd9');
