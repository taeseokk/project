package com.example.project.repository.post;

import com.example.project.dto.post.PostSearchCondition;
import com.example.project.entity.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<Post> search(PostSearchCondition postSearchCondition, Pageable pageable);

}
