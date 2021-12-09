package com.tw.bootcamp.bookshop.admin.documentation;

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
@Operation(summary = "Upload books", description = "Upload books for book shop", tags = {"Book Service"})
@ApiResponse(responseCode = "200", description = "Uploaded books successfully",
        content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Book.class))
        ))
@ApiResponse(responseCode = "400", description = "Upload a valid csv file",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(responseCode = "401", description = "Unauthorised. Only Admin can upload books",
        content = @Content(schema = @Schema()))
public @interface LoadBookDocumentation {
}
