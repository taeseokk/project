package com.example.project.dto.member;

import com.example.project.entity.member.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDto {

    private String id;
    private String name;
    private String nickname;
    private Integer age;


    @Builder
    public MemberInfoDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.nickname = member.getNickName();
        this.age = member.getAge();
    }

}