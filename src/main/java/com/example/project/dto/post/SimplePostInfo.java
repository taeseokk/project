package com.example.project.dto.post;

import com.example.project.entity.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimplePostInfo {

    private Long postId;	   // 댓글 idx
    private String title;	   // 제목
    private String content;	   // 내용
    private String MemberName; // 작성자
    private String createDate; // 작성일

    public SimplePostInfo(Post post) {
        this.postId = post.getIdx();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.MemberName = post.getMember().getName();
        this.createDate = post.getCreateTime().toString();
    }
}
