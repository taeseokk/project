package com.example.project.dto.commet;

import com.example.project.dto.member.MemberInfoDto;
import com.example.project.entity.comment.Comment;
import lombok.Data;

@Data
public class ReCommentInfoDto {

    private final static String DEFAULT_MESSAGE = "삭제 댓글";

    private Long postId;
    private Long parentId;
    private Long reCommentId;
    private String content;
    private boolean isRemoved;
    private MemberInfoDto writerDto;

    public ReCommentInfoDto(Comment reComment) {
        this.postId = reComment.getPost().getIdx();
        this.parentId = reComment.getParent().getIdx();
        this.reCommentId = reComment.getIdx();
        this.content = reComment.getContent();

        if(reComment.isRemoved()) {
            this.content = DEFAULT_MESSAGE;
        }
        this.isRemoved = reComment.isRemoved();
        this.writerDto = new MemberInfoDto(reComment.getMember());
    }
}
