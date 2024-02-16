package com.example.project.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResult<T> extends CommonResult {

    private List<T> list;   // list로 응답을 출력하기 위해서
}
