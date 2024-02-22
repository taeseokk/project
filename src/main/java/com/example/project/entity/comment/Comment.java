package com.example.project.entity.comment;

import com.example.project.entity.BaseTime;
import com.example.project.entity.member.Member;
import com.example.project.entity.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="commentId")
    private Long idx;

    @Lob
    @Column
    private String content;

    @Column
    @Builder.Default
    private boolean isRemoved=false;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Comment parent;

    // 대댓글 기능을 구현하기 위해서
    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    public void remove() {
        this.isRemoved = true;
    }

    //자식댓글(대댓글) 삭제 확인 메소드
    private boolean childAllRemoved() {

        return getChildList().stream()
                .map(Comment :: isRemoved)  //map : 요소들을 특정조건에 해당하는 값으로 변환(Comment엔티티 isRemoved를 지워졌는지 여부로 바꾼다.)
                .filter(isRemove -> !isRemove) //메소드 참조방식 : 지워졌으면 true, 안지워졌으면 false -> filter를 통해 걸려지는 것은 false
                .findAny()	//지워지지 않은 댓글이 있으면 false(순서에 상관없이 특정 요소를 찾는 것,찾는 값을 찾을 시 실행 종료) / Parallel -> 병렬처리
                .orElse(true); //모두 지워진 상황이면 true
    }

    public List<Comment> findRemovableList(){
        List<Comment> list = new ArrayList<>();
        //optional.ofNullable : null인지 아닌지 확신할 수 없는 개체를 담고 있는 객체
		/* ifpresentOrElse : ifpresent()와 유사하며 optional 객체가 비어있을 경우 처리할 내용까지 정의
		   두개의 인자를 반는다(optional객체가 값을 담고 있을 때, optional객체가 비어있을 경우)*/
        Optional.ofNullable(this.parent).ifPresentOrElse(
                parentComment->{	//대댓글인 경우(부모(댓글) 존재)
                    if(parentComment.isRemoved()&&parentComment.childAllRemoved()) {	//isRemoved : true는 삭제가 되고 && 모든 자식 댓글이 삭제가 되었을 시 true
                        list.addAll(parentComment.getChildList()); //addAll : Collection에 있는 아이템들을 모두 리스트에 추가
                        list.add(parentComment);
                    }
                }, ()->{ // 대댓글이 없는 경우(일반 댓글)
                    if(childAllRemoved()) {
                        list.add(this);
                        list.addAll(this.getChildList());
                    }
                });
        return list;
    }

    public void checkPost(Post post) {
        this.post = post;
        post.addComment(this);
    }

    public void checkMember(Member member) {
        this.member = member;
        member.addComment(this);
    }

    public void checkParent(Comment parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Comment child) {
        childList.add(child);
    }

}
