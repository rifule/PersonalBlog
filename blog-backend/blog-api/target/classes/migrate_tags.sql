-- ============================================
-- 标签关联表数据迁移脚本
-- 将 tb_article 表中的 tags 字段迁移到 tb_article_tag 关联表
-- ============================================

USE blog_db;

-- 开始事务
START TRANSACTION;

-- 1. 创建临时表存储需要迁移的数据
CREATE TEMPORARY TABLE IF NOT EXISTS temp_article_tags AS
SELECT 
    a.id AS article_id,
    a.tags
FROM tb_article a
WHERE a.tags IS NOT NULL 
  AND a.tags != ''
  AND a.deleted = 0;

-- 2. 清空关联表（如果已有数据）
-- DELETE FROM tb_article_tag WHERE deleted = 0;

-- 3. 将逗号分隔的标签名称转换为标签ID并插入关联表
INSERT INTO tb_article_tag (article_id, tag_id, create_time, deleted)
SELECT 
    t.article_id,
    tag.id AS tag_id,
    NOW() AS create_time,
    0 AS deleted
FROM temp_article_tags t
CROSS JOIN JSON_TABLE(
    CONCAT('["', REPLACE(t.tags, ',', '","'), '"]'),
    '$[*]' COLUMNS (tag_name VARCHAR(50) PATH '$')
) AS jt
INNER JOIN tb_tag tag ON tag.name = jt.tag_name
WHERE tag.deleted = 0
ON DUPLICATE KEY UPDATE deleted = 0;

-- 4. 删除临时表
DROP TEMPORARY TABLE IF EXISTS temp_article_tags;

-- 5. 验证迁移结果
SELECT 
    '迁移统计' AS info,
    COUNT(DISTINCT article_id) AS article_count,
    COUNT(*) AS total_relations
FROM tb_article_tag
WHERE deleted = 0;

-- 提交事务
COMMIT;

-- ============================================
-- 可选：迁移完成后删除 article 表的 tags 字段
-- 注意：请先确认数据迁移成功后再执行
-- ============================================
-- ALTER TABLE tb_article DROP COLUMN tags;
