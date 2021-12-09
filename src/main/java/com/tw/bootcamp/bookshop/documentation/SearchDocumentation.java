package com.tw.bootcamp.bookshop.documentation;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "Search books", description = "Search books by title and author from book shop", tags = {"Book Service"})
@ApiResponse(responseCode = "200", description = "Search books successful",
        content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Book.class))
        ))
@ApiResponse(responseCode = "400", description = "User must search with either title or author",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(responseCode = "401", description = "Unauthorised user",
        content = @Content(schema = @Schema()))
@ApiResponse(responseCode = "204", description = "No books found with matching search criteria",
        content = @Content(schema = @Schema()))
public @interface SearchDocumentation {

}
