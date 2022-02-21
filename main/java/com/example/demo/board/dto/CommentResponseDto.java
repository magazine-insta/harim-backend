package com.example.demo.board.dto;

import com.example.demo.board.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private String commentUser;
    private String title;


    public CommentResponseDto toDto(Comment comment){
        return CommentResponseDto.builder()
            .id(comment.getId())
            .title(comment.getTitle())
            .description(comment.getDescription())
            .commentUser(comment.getCommentUser())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();
    }

}
