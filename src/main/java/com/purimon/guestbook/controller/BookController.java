package com.purimon.guestbook.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.purimon.guestbook.domain.GuestBook;
import com.purimon.guestbook.service.GuestBookService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller("bookController")
@RequestMapping("/book")
public class BookController {
    private final GuestBookService guestBookService;

    private List<GuestBook> bookList;

    @GetMapping("/list")
    public String getBookList(Model model) {
        bookList = guestBookService.listBooks();
        model.addAttribute("dataList", bookList);
        return "list";
    }

    @GetMapping("/add")
    public String writePost() {
        return "write";
    }

    @PostMapping("/post")
    public String posting(@RequestParam String post, @RequestParam String writer) {
        GuestBook book = new GuestBook();
        book.setPost(post);
        book.setWriter(writer);

        guestBookService.addPost(book);
        return "redirect:list";
    }
}
