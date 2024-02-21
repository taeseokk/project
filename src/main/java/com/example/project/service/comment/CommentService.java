package com.example.project.service.comment;

import com.example.project.dto.commet.CommentSaveDto;
import com.example.project.dto.commet.CommentUpdateDto;
import com.example.project.entity.comment.Comment;

public interface CommentService {

    void save(Long postId, CommentSaveDto commentSaveDto);

    void saveReComment(Long postId, Long parentId, CommentSaveDto commentSaveDto);

    Comment findByIdx(Long idx) throws Exception;

    void update(Long id, CommentUpdateDto commentUpdateDto);

    // List<Comment> findAll();

    void remove(Long idx) throws Exception;
}
