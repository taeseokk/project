package com.example.project.entity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult {

    private  T data;    // 응답 값
}
