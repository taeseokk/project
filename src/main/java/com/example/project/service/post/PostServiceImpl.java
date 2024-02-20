package com.example.project.service.post;

import com.example.project.dto.post.PostDto;
import com.example.project.entity.post.Post;
import com.example.project.repository.member.MemberRepository;
import com.example.project.repository.post.PostRepository;
import com.example.project.security.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Override
    public void savePost(PostDto postDto) {
        Post post = Post.builder().title(postDto.getTitle()).content(postDto.getContent()).build();
        post.setMember(memberRepository.findById(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new Exception()));

        postRepository.save(post);
    }

    @Override
    public Post updatePost(Long idx, PostDto postDto) {
        Post post = postRepository.findById(idx).orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));
        checkAuthority(post, PostExceptionType.NOT_AUTHORITY_UPDATE_POST);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        if (post.getFilePath() != null) {
            fileService.deletePath(post.getFilePath());
        }
        for (MultipartFile mf : postDto.getUploadFile()) {
            if (mf != null) {
                post.setFilePath(fileService.save(mf));
            } else {
                post.setFilePath(null);
            }
        }
//ifPresentOrElse : 첫번째 ifPresent, 두번째 : optional 비어있는 경우
// ifPresent : 값을 가지고 있는지 확인 후 예외처리를 통해 값이 넘어감
        return post;
    }

    @Override
    public void deletePost(Long idx) {
        Post post = postRepository.findById(idx).orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));
        checkAuthority(post, PostExceptionType.NOT_AUTHORITY_DELETE_POST);
        if (post.getFilePath() != null) {
            fileService.deletePath(post.getFilePath());
        }
        postRepository.delete(post);
    }

    @Override
    public PostInfoDto searchPost(Long idx) {

        return new PostInfoDto(postRepository.findWithMemberByIdx(idx).orElseThrow(() -> new NotFoundMemberException()));
    }

    @Override
    public PostPagingDto searchList(Pageable pageable, PostSearchCondition search) {

        return new PostPagingDto(postRepository.search(search, pageable));
    }

    private void checkAuthority(Post post, PostExceptionType exceptionType) { // 권한 체크
        if (!post.getMember().getId().equals(SecurityUtil.getLoginUsername())) { //
            throw new PostException(exceptionType);
        }
    }
}
