package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.PostCategory;
import com.yeokeong.gonggang.model.entity.PostCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryId> {
}
