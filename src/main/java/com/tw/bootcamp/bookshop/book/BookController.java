package com.tw.bootcamp.bookshop.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/books")
@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    List<Book> list() {
        List<Book> books = bookService.fetchAll();
        return books;
    }

    @PostMapping
    List<Book> upload(@RequestParam(name="file") MultipartFile inputBookList) throws IOException, InvalidFileFormatException {
        return bookService.upload(inputBookList);
    }

    @PostMapping("/search")
    List<Book> search(@RequestBody SearchRequest searchRequest) throws InvalidRequestParameterException {
        return bookService.search(searchRequest.getTitle(), searchRequest.getAuthor());
    }
}
