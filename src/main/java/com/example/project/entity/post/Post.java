package com.example.project.entity.post;

import com.example.project.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId")
    private Long idx;

    @Lob
    @Column
    private String content;

    @Column
    private String filePath;

    @Column
    private String title;
}
