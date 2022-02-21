package com.example.demo.board.dto;

import com.example.demo.board.domain.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardRequestDto {
    private String title;
    private String description;
    private String user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Board toEntity(){
        return Board.builder()
                .createdAt(LocalDateTime.now())
                .title(this.getTitle())
                .user(this.user)
                .description(this.description)
                .build();
    }
}
