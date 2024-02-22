package com.example.project.entity.post;

import com.example.project.entity.BaseTime;
import com.example.project.entity.comment.Comment;
import com.example.project.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId")
    private Long idx;

    @Lob
    @Column
    private String content;

    @Column
    private String filePath;

    @Column
    private String title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    // 게시글이 삭제가 될 경우 댓글도 같이 삭제
    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public void addComment(Comment comment) {	//comment의 post 설정은 comment에서 한다.
        commentList.add(comment);
    }
    public void checkWriter(Member member) {
        this.member = member;
        member.addPost(this);
    }
}
