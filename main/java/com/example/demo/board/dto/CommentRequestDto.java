package com.example.demo.board.dto;

import com.example.demo.board.domain.Board;
import com.example.demo.board.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class CommentRequestDto {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private String commentUser;
    private String title;

    public Comment toEntity(Board board){
        return Comment.builder()
                .createdAt(LocalDateTime.now())
                .title(this.title)
                .commentUser(this.commentUser)
                .description(this.description)
                .board(board)
                .build();
    }
}
