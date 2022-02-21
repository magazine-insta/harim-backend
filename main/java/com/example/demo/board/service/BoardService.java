package com.example.demo.board.service;

import com.example.demo.board.domain.Board;
import com.example.demo.board.domain.BoardRepository;
import com.example.demo.board.domain.Comment;
import com.example.demo.board.domain.CommentRepository;
import com.example.demo.board.dto.BoardRequestDto;
import com.example.demo.board.dto.BoardResponseDto;
import com.example.demo.board.dto.CommentRequestDto;
import com.example.demo.board.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public List<BoardResponseDto> showAllBoard() {

        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(); // findAll();
        return boards.stream()
                .map(board -> {
                    BoardResponseDto boardResponseDto = new BoardResponseDto();
                    return boardResponseDto.toDto(board);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardResponseDto showBoard(Long boardId) {

        return boardRepository.findById(boardId).get().toDto();
    }

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto){
        Board board = boardRepository.save(boardRequestDto.toEntity());
        BoardResponseDto boardResponseDto = new BoardResponseDto();
        return boardResponseDto.toCreatedDto(board);
    }

    @Transactional
    public Long updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Optional<Board> board = boardRepository.findById(boardId);
        board.get().update(boardRequestDto); // ...
        return boardId;
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    //댓글
    @Transactional
    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto){

        Board board = boardRepository.findById(boardId).orElse(new Board());
        Comment comment = commentRequestDto.toEntity(board);
        commentRepository.save(comment);
        //board.addComment(comment);

        return comment.toDto();
    }

    @Transactional
    public List<CommentResponseDto> showComments(Long boardId){

        return commentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId).stream().map(Comment::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Long updateComment(Long commentId, CommentRequestDto commentRequestDto){

        Comment comment = commentRepository.findById(commentId).orElse(new Comment());
        comment.update(commentRequestDto);

        return commentId;
    }

    @Transactional
    public void deleteComment(Long commentId){

        commentRepository.deleteById(commentId);
    }

}
