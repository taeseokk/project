package com.example.project.dto.post;

import com.example.project.entity.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class PostPagingDto {

    private int totalPageCount;   // 총 페이지 수
    private int currentPageNum;   // 현재 페이지
    private long totalCount;      // 존재하는 게시글 총 수
    private int currentPageCount; // 현재 페이지에 존재하는 게시글 수


    public PostPagingDto(Page<Post> search) {
        this.totalPageCount = search.getTotalPages();
        this.currentPageNum = search.getNumber();
        this.totalCount = search.getTotalElements();
        this.currentPageCount = search.getNumberOfElements();
    }
}