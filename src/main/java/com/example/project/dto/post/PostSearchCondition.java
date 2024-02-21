package com.example.project.dto.post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSearchCondition {

    private String title;
    private String content;
}
