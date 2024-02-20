package com.example.project.dto.commet;

import com.example.project.dto.member.MemberInfoDto;
import com.example.project.entity.comment.Comment;
import lombok.Data;

import java.util.List;

@Data
public class CommentInfoDto {

    private final static String DEFAULT_MESSAGE = "삭제 댓글";

    private Long postId;
    private Long commentId;
    private String content; // 내용(삭제된 댓글일 경우 DEFAULT_MESSAGE 삭제 댓글 출력)
    private boolean isRemoved; // 삭제 유무

    private MemberInfoDto writeDto; // 댓글 작성자 정보

 //   private List<ReCommentInfoDto> reCommentListDtoList; // 대댓글 정보

    public CommentInfoDto(Comment comment, List<Comment> reCommentList) {
        this.postId = comment.getPost().getIdx();
        this.commentId = comment.getIdx();
        this.content = comment.getContent();
        if (comment.isRemoved()) {
            this.content = DEFAULT_MESSAGE;
        }
        this.isRemoved = comment.isRemoved();
        this.writeDto = new MemberInfoDto(comment.getMember());
//        this.reCommentListDtoList = reCommentList.stream().map(ReCommentInfoDto::new).collect(Collectors.toList());
    }
}
