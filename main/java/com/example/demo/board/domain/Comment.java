package com.example.demo.board.domain;

import com.example.demo.board.dto.CommentRequestDto;
import com.example.demo.board.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
public class Comment {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private String commentUser;
    private String title;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;


    public CommentResponseDto toDto(){
        return CommentResponseDto.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .commentUser(this.commentUser)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.title = commentRequestDto.getTitle();
        this.description = commentRequestDto.getDescription();
        this.commentUser = commentRequestDto.getCommentUser();
        this.updatedAt = LocalDateTime.now();
    }
}
