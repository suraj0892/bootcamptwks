package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.documentation.SearchDocumentation;
import com.tw.bootcamp.bookshop.book.exceptions.InvalidRequestParameterException;
import com.tw.bootcamp.bookshop.book.exceptions.NoBooksFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/books")
@RestController
@Tag(name = "Book Service", description = "APIs for book service")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "List books", description = "List all the books from book shop", tags = {"Book Service"},parameters = {})
    @GetMapping
    List<Book> list(@RequestParam(required = false) String sortByPrice) {

        List<Book> books = sortByPrice==null ? bookService.fetchAll() : bookService.
                fetchAll(sortByPrice);
        return books;
    }

    @PostMapping("/search")
    @SearchDocumentation
    List<Book> search(@RequestBody SearchRequest searchRequest) throws InvalidRequestParameterException, NoBooksFoundException {
        return bookService.search(searchRequest.getTitle(), searchRequest.getAuthor());
    }
}
