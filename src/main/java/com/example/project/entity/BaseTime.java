package com.example.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass   //JPA의 엔티티 클래스가 상속받을 경우 자식 클래스에게 매필 정보를 전달
@EntityListeners(AuditingEntityListener.class)  // 엔티티를 DB에 적용하기 전후로 콜백을 요청할 수 있게 해준다(엔티티의 Auditing 정보를 주입하는 JPA 엔티티 리스너 클래스)
public class BaseTime {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime CreateTime;


    @LastModifiedDate
    private LocalDateTime modifiedTime;

}
