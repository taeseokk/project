package com.example.project.repository.post;

import com.example.project.entity.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{

    // EntityGraph를 이용하여 post와 member 쿼리 한번에 가져오기 -> 패치조인을 간편하게 사용할 수 있도록 해주는 어노테이션(연관된 엔티티를 한번에 조회)
    // -> select p from Post p join fetch p.member m where p.idx=idx"
    //패치조인은 내가 원하는 시점에 원하는 데이터, 객체 그래프를 조회할 거라는 걸 직접 명시적으로 동적인 타이밍에 정할 수 있다.
    @EntityGraph(attributePaths = { "member" })
    Optional<Post> findWithMemberByIdx(Long idx);

}