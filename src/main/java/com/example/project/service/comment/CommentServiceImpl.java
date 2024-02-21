package com.example.project.service.comment;

import com.example.project.dto.commet.CommentSaveDto;
import com.example.project.dto.commet.CommentUpdateDto;
import com.example.project.entity.comment.Comment;
import com.example.project.repository.comment.CommentRepository;
import com.example.project.repository.member.MemberRepository;
import com.example.project.repository.post.PostRepository;
import com.example.project.security.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional // 클래스에 선언할 경우 해당 클래스에 속한 메서드에 공통 적용
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void save(Long postId, CommentSaveDto commentSaveDto) {
        Comment comment = Comment.builder().content(commentSaveDto.getContent()).build();
        comment.checkMember(memberRepository.findById(SecurityUtil.getLoginUsername()).orElseThrow(() -> new NotFoundMemberException()));
        comment.checkPost(postRepository.findById(postId).orElseThrow(()->new NotFoundMemberException()));
        commentRepository.save(comment);
    }

    @Override
    public void saveReComment(Long postId, Long parentId, CommentSaveDto commentSaveDto) {
        Comment comment = Comment.builder().content(commentSaveDto.getContent()).build();
        comment.checkMember(memberRepository.findById(SecurityUtil.getLoginUsername()).orElseThrow(()-> new NotFoundMemberException()));
        comment.checkPost(postRepository.findById(parentId).orElseThrow(()-> new NotFoundMemberException()));
        comment.checkParent(commentRepository.findById(parentId).orElseThrow(()-> new NotFoundMemberException()));

        commentRepository.save(comment);

    }

    @Override
    public void update(Long id, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundMemberException());
        if(!comment.getMember().getId().equals(SecurityUtil.getLoginUsername())) {
            throw new NotFoundMemberException();
        }
        comment.setContent(commentUpdateDto.getContent());

    }

    @Override
    @Transactional(readOnly = true) // 일기만 가능 수정x
    public Comment findByIdx(Long idx) throws Exception {

        return commentRepository.findById(idx).orElseThrow(() -> new Exception("댓글 x"));
    }

    @Override
    public void remove(Long idx) throws Exception {
        Comment comment = commentRepository.findById(idx).orElseThrow(() -> new Exception("댓글 x")); // 기본키idx를 통하여
        // comment 객체 가져오기
        comment.remove(); // 삭제가 되었다면 삭제를 true변경
        List<Comment> removeCommentList = comment.findRemovableList();
        removeCommentList.forEach(rc -> commentRepository.delete(rc));

        commentRepository.delete(comment);

    }

//	@Override
//	public List<Comment> findAll() {
//
//		return commentRepository.findAll();
//	}

}
/*
 * @Transaction 특징 CheckedException or 예외가 없을 때 commit UnCheckedException이 발생하면
 * Rollback
 */
// orElseThrow : Optional에서 원하는 객체를 바로 얻거나 예외를 던질 때(Optional의 인자가 null일 경우 예외처리,findbyId가 optional을 반환하기 때문에)
