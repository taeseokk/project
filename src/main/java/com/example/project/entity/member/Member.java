package com.example.project.entity.member;


import com.example.project.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long idx;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String nickName;

    @Column
    private int age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 1000)
    private String refreshToken;


    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword) {
        return passwordEncoder.matches(checkPassword, getPassword());
    }
}
