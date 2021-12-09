package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.book.documentation.SearchDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/books")
@RestController
@Tag(name = "Book Service", description = "APIs for book service")
public class BookController{
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "List books", description = "List all the books from book shop", tags = {"Book Service"})
    @GetMapping
    List<Book> list() {
        List<Book> books = bookService.fetchAll();
        return books;
    }

    @PostMapping("/search")
    @SearchDocumentation
    List<Book> search(@RequestBody SearchRequest searchRequest) throws InvalidRequestParameterException, NoBooksFoundException {
        return bookService.search(searchRequest.getTitle(), searchRequest.getAuthor());
    }
}
