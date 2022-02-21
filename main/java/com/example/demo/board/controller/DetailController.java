package com.example.demo.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetailController {
    @GetMapping("/board/{boardId}")
    public String getDetail(Model model){
        model.addAttribute("boardId",1);
        return "detail";
    }
}
