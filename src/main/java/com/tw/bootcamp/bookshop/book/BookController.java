package com.tw.bootcamp.bookshop.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/books")
    List<Book> upload(@RequestParam(name="file") MultipartFile inputBookList) throws IOException {
        return bookService.upload(inputBookList);
    }

    @PostMapping("/search")
    List<Book> search(@RequestBody SearchRequest searchRequest) {
        return bookService.search(searchRequest.getTitle(), searchRequest.getAuthor());
    }
}
