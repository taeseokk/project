package com.example.project.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDto {

    @NotBlank(message = "제목 입력")
    private String title;

    @NotBlank(message = "내용 입력")
    private String content;

    List<MultipartFile> uploadFile;
}

// MultipartFile : 업로드 한 파일 정보 및 파일 데이터를 표현하기 위한 용도로 사용, HTTP프로토콜의 바디 부분에 데이터를 여러 부분으로 나눠서 보내는 것

