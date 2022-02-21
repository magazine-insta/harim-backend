package com.example.demo.board.dto;

import com.example.demo.board.domain.Board;
import com.example.demo.board.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;

    private String title;
    private String description;
    private String user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentResponseDto> comments;


    public BoardResponseDto toCreatedDto(Board board) {

         return  BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .description(board.getDescription())
                .user(board.getUser())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
//                .comments(board.getComments()
//                        .stream().map(comment -> {
//                            CommentResponseDto commentResponseDto = new CommentResponseDto();
//                            return commentResponseDto.toDto(comment);})
//                        .collect(Collectors.toList())
//                )
                .build();
    }

    public BoardResponseDto toDto(Board board) {

        return  BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .description(board.getDescription())
                .user(board.getUser())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .comments(board.getComments()
                        .stream().map(comment -> {
                            CommentResponseDto commentResponseDto = new CommentResponseDto();
                            return commentResponseDto.toDto(comment);})
                        .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                        .collect(Collectors.toList())
                )
                .build();
    }

}
