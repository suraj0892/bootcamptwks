package com.tw.bootcamp.bookshop.admin;

import com.tw.bootcamp.bookshop.admin.documentation.LoadBookDocumentation;
import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.InvalidFileFormatException;
import com.tw.bootcamp.bookshop.error.ErrorResponse;
import com.tw.bootcamp.bookshop.user.UserView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@RestController
@Tag(name = "Admin Service", description = "APIs for Admin service")
public class AdminController {

    private final BookService bookService;

    @Autowired
    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @LoadBookDocumentation
    @PostMapping(value = "/books/load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<Book> upload(@RequestPart("file") MultipartFile inputBookList) throws IOException, InvalidFileFormatException {
        return bookService.upload(inputBookList);
    }
}