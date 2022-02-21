package com.example.demo.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Likes {
    @Id
    @GeneratedValue
    private java.lang.Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
    private Long user;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
    private Long post;

}
