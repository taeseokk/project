package com.example.project.entity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    private boolean success; // 성공 유무 : true / false

    private int code;        // 코드 : 음수 값이면 실패

    private String msg;      // 설명 : msg 세부 설명
}
