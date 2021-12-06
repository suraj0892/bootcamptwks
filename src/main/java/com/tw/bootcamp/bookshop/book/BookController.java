package com.tw.bootcamp.bookshop.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    List<Book> list() {
        List<Book> books = bookService.fetchAll();
        return books;
    }

    @PostMapping("/search")
    List<Book> search(@RequestBody SearchRequest searchRequest) {
        return bookService.search(searchRequest.getTitle(), searchRequest.getAuthor());
    }
}
