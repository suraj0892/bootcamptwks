package com.tw.bootcamp.bookshop.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@WithMockUser
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    UserService userService;

    @Test
    void shouldListAllBooksWhenPresent() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new BookTestBuilder().build();
        books.add(book);
        when(bookService.fetchAll()).thenReturn(books);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).fetchAll();
    }

    @Test
    void shouldBeEmptyListWhenNoBooksPresent() throws Exception {
        when(bookService.fetchAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(bookService, times(1)).fetchAll();
    }

    @Test
    void shouldListBooksWithMatchedTitleWhenPresent() throws InvalidRequestParameterException, Exception, NoBooksFoundException {
        SearchRequest searchRequest = new SearchRequest("Wings of Fire", "");
        List<Book> books = new ArrayList<Book>() {{
            new BookTestBuilder().withName(searchRequest.getTitle()).build();
        }};
        when(bookService.search(searchRequest.getTitle(), searchRequest.getAuthor())).thenReturn(books);

        mockMvc.perform(post("/books/search")
                        .content(objectMapper.writeValueAsString(searchRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(books)));
        verify(bookService, times(1)).search(searchRequest.getTitle(), searchRequest.getAuthor());
    }

    @Test
    @WithMockUser(username = "admin@test.com", password = "admin", roles = "ADMIN")
    void shouldBeAbleToSeeBookListForAdminRole() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }
}