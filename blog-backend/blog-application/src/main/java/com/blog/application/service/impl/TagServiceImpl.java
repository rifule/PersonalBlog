package com.blog.application.service.impl;

import com.blog.application.service.TagService;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCode;
import com.blog.domain.entity.Tag;
import com.blog.domain.repository.ArticleTagRepository;
import com.blog.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    @Override
    @Transactional
    public Tag createTag(Tag tag) {
        System.out.println("Creating tag: name=" + tag.getName() + ", color=" + tag.getColor());
        tagRepository.insert(tag);
        return tag;
    }

    @Override
    @Transactional
    public Tag updateTag(Long id, Tag tag) {
        System.out.println("Updating tag: id=" + id + ", name=" + tag.getName() + ", color=" + tag.getColor());
        Tag existing = tagRepository.selectById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.ERROR);
        }

        existing.setName(tag.getName());
        existing.setColor(tag.getColor());

        tagRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> getAllTags() {
        List<Tag> tags = tagRepository.findAllOrderByCreateTimeDesc();
        // 统计每个标签的文章数量
        for (Tag tag : tags) {
            List<Long> articleIds = articleTagRepository.selectArticleIdsByTagId(tag.getId());
            tag.setArticleCount(articleIds.size());
        }
        return tags;
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag = tagRepository.selectById(id);
        if (tag == null) {
            throw new BusinessException(ResultCode.ERROR);
        }
        return tag;
    }
}
