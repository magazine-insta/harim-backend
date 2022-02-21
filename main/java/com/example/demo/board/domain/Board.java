package com.example.demo.board.domain;

import com.example.demo.board.dto.BoardRequestDto;
import com.example.demo.board.dto.BoardResponseDto;
import com.example.demo.board.dto.CommentResponseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
@Setter
public class Board {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String title;
    private String description;
    private String user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.PERSIST)
    private List<Comment> comments;

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
        this.user = boardRequestDto.getUser();
        this.updatedAt = LocalDateTime.now();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public BoardResponseDto toDto() {
        return  BoardResponseDto.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .user(this.user)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .comments(this.comments.stream().map(Comment::toDto).collect(Collectors.toList())
                )
                .build();
    }
}
