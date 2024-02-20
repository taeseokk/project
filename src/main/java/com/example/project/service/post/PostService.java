package com.example.project.service.post;

import com.example.project.dto.post.PostDto;
import com.example.project.dto.post.PostInfoDto;
import com.example.project.entity.post.Post;

import java.awt.print.Pageable;

public interface PostService {

    void savePost(PostDto postDto);

    Post updatePost(Long idx, PostDto postDto);

    void deletePost(Long idx);

    PostInfoDto searchPost(Long idx);

//    PostPagingDto searchList(Pageable pageable, PostSearchCondition search);


}

/* NotNull  : null만 허용 x / "", " " 이처럼 공백은 허용 가능
 * NotEmpty : null, "" 허용 x / " " 같은 공백은 허용 가능 -> NotNull + ""
 * NotBlank : null, "", " " 모두 허용 x
 */

