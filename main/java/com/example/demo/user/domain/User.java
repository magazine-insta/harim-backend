package com.example.demo.user.domain;

import com.example.demo.post.domain.Post;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "posts")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;
    
    private String nickname;

    @Column(nullable = false)
    private String userPwd;

    private String userImg; // url

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE })
    private List<Post> posts;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE })
//    private List<Likes> likes;
}
