package com.example.project.service.file;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${spring.file.dir}")
    private String fileDir;

    @Override
    public String save(MultipartFile multipartFile) {
        String filePath = fileDir + UUID.randomUUID(); // 파일을 저장할 경우 이름이 겹치지 않게 저장하기 위해서
        try {
            multipartFile.transferTo(new File(filePath));// transferTo 메소드를 이용해서 업로드(저장) 처리 메서드
        } catch (IOException e) {
            throw new FileException(FileExceptionType.FILE_CAN_NOT_SAVE);
        }
        return filePath;
    }

    @Override
    public void deletePath(String filepath) {
        File file = new File(filepath);
        if (!file.exists())
            return; // 파일이 존재하지 않을 때

        if (!file.delete())
            throw new FileException(FileExceptionType.FILE_CAN_NOT_DELETE);// 파일을 삭제하지 못했을 경우

    }

}