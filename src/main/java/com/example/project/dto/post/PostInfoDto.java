package com.example.project.dto.post;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.project.dto.commet.CommentInfoDto;
import com.example.project.dto.member.MemberInfoDto;
import com.example.project.entity.comment.Comment;
import com.example.project.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostInfoDto {

    private Long idx;
    private String content;
    private String filePath;
    private String title;
    private MemberInfoDto writerDto;// 작성자에 대한 정보
    private List<CommentInfoDto> commentInfoDtoList;// 댓글 정보들

    public PostInfoDto(Post post) {

        this.idx = post.getIdx();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.filePath = post.getFilePath();

        this.writerDto = new MemberInfoDto(post.getMember());	//getMember : memberId

        //<댓글,List<해당 댓글에 달린 대댓글>>
        Map<Comment, List<Comment>> commentListMap = post.getCommentList().stream() //post에서 comment 리스트를 가져오기(commentList에는 댓글과 대댓글이 섞여 있는 상황)
                .filter(comment -> comment.getParent() != null)	//parent가 null이 아닌 -> 대댓글인 것들
                .collect(Collectors.groupingBy(Comment::getParent)); // 필터링된 것은 모두 대댓글,대댓글의 parent(댓글)를 통해 그룹핑

        //Map에서 key값 : 댓글을 가져오는 것
        commentInfoDtoList = commentListMap.keySet().stream()
                .map(comment -> new CommentInfoDto(comment,commentListMap.get(comment)))
                .collect(Collectors.toList());
    }
}
// Collectors.toList 와 toList()는 똑같은 형태의 구현체로 반환되지 않는다.
// Collectors.toList : ArrayList로 반환, null을 허용
// stream.toList() : null 허용
