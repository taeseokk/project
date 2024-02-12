package com.example.project.entity.comment;

import com.example.project.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="commentId")
    private Long idx;

    @Lob
    @Column
    private String content;

    @Column
    @Builder.Default
    private boolean isRemoved=false;

}
