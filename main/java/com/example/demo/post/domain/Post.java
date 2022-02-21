package com.example.demo.post.domain;


import com.example.demo.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "user")
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String contentText;
    private String contentImg;
    private String imageFileName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int likeCnt;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE })
//    private List<Likes> likes;

}
