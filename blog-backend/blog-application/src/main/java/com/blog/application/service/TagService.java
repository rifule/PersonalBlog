package com.blog.application.service;

import com.blog.domain.entity.Tag;

import java.util.List;

public interface TagService {

    Tag createTag(Tag tag);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    List<Tag> getAllTags();

    Tag getTagById(Long id);
}
