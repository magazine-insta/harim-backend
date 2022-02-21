package com.example.demo.post.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikesRepository extends CrudRepository<Likes, Long> {

    Optional<Likes> findIdByUserAndPost(Long userId, Long id);
}
