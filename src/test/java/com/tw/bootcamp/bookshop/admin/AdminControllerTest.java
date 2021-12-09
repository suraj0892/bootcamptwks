package com.tw.bootcamp.bookshop.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.exceptions.InvalidFileFormatException;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(username = "suraj1.krishnan@thoughtworks.com", password = "password", roles = "USER")
    void ShouldNotBeAbleToUploadBooksWithRoleAsUser() throws Exception, InvalidFileFormatException {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv",
                "text/csv", "".getBytes());
        when(bookService.upload(file)).thenReturn(new ArrayList<>());

        mockMvc.perform(multipart("/admin/books/load").file(file))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "suraj.krishnan@thoughtworks.com", password = "password", roles = "ADMIN")
    void ShouldBeAbleToUploadBooksForAdminRole() throws Exception, InvalidFileFormatException {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv",
                "text/csv", "".getBytes());
        when(bookService.upload(file)).thenReturn(new ArrayList<>());

        mockMvc.perform(multipart("/admin/books/load").file(file))
                .andExpect(status().isOk());

        verify(bookService, times(1)).upload(file);
    }

    @Test
    @WithMockUser(username = "suraj.krishnan@thoughtworks.com", password = "password", roles = "ADMIN")
    void ShouldThrowExceptionWhenFileIsMissingForAdminRole() throws Exception {
        mockMvc.perform(post("/admin/books/load")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MissingServletRequestPartException));
    }


}