package com.example.demo.board.controller;

import com.example.demo.board.dto.BoardRequestDto;
import com.example.demo.board.dto.BoardResponseDto;
import com.example.demo.board.dto.CommentRequestDto;
import com.example.demo.board.dto.CommentResponseDto;
import com.example.demo.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;





    @GetMapping("/api/board")
    public List<BoardResponseDto> getBoards(){
        return boardService.showAllBoard();
    }

    @GetMapping("/api/board/{boardId}")
    public BoardResponseDto getBoard(@PathVariable Long boardId){
        return boardService.showBoard(boardId);
    }

    @PostMapping("/api/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardRequestDto){
        return boardService.createBoard(boardRequestDto);
    }

    @PatchMapping("/api/board/{boardId}")
    public Long updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto boardRequestDto){
        return boardService.updateBoard(boardId, boardRequestDto);
    }

    @DeleteMapping("/api/board/{boardId}")
    public void deleteBoard(@PathVariable Long boardId){
        boardService.deleteBoard(boardId);
    }


    @GetMapping("/api/board/{boardId}/comments")
    public List<CommentResponseDto> showComments(@PathVariable Long boardId){
        return boardService.showComments(boardId);
    }

    @PostMapping("/api/board/{boardId}/comments")
    public CommentResponseDto createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto){
        return boardService.createComment(boardId, commentRequestDto);
    }

    @PatchMapping("/api/board/{boardId}/comments/{commentId}")
    public Long updateComment(@PathVariable Long boardId,@PathVariable Long commentId,@RequestBody CommentRequestDto commentRequestDto){
        return boardService.updateComment(commentId, commentRequestDto);
    }

    @DeleteMapping("/api/board/{boardId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        boardService.deleteComment(commentId);
    }
}
