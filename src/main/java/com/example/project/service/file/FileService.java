package com.example.project.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    // 저장된 파일 경로 반환
    String save(MultipartFile multipartFile);

    void deletePath(String filepath);

}