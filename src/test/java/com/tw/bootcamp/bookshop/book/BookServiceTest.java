package com.tw.bootcamp.bookshop.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldFetchAllBooks() {
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);

        List<Book> books = bookService.fetchAll();

        assertEquals(1, books.size());
        assertEquals("title", books.get(0).getName());
    }

    @Test
    void shouldFetchAllBooksBeSortedByNameAscending() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.fetchAll();

        assertEquals(2, books.size());
        assertEquals("Animal Farm", books.get(0).getName());
    }


    @Test
    void ShouldBeAbleToLoadBooks() throws IOException, InvalidFileFormatException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream bookList = classloader.getResourceAsStream("test.csv");
        MockMultipartFile file = new MockMultipartFile("file", "test.csv","text/csv",bookList );

        List<Book> books = bookService.upload(file);

        assertEquals(1, books.size());
        assertEquals(books.get(0).getAuthorName(), "Cassandra Clare");
    }

    @Test
    void ShouldNotBeAbleToLoadWhenThereIsValidationError() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream bookList = classloader.getResourceAsStream("error.csv");
        MockMultipartFile file = new MockMultipartFile("file", "error.csv","text/csv",bookList );

        assertThrows(Exception.class, ()-> bookService.upload(file));
    }

    @Test
    void ShouldNotBeAbleToLoadWhenInputFileIsNotCsv() throws IOException, InvalidFileFormatException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream bookList = classloader.getResourceAsStream("error.txt");
        MockMultipartFile file = new MockMultipartFile("file", "error.txt","plain/txt",bookList );

       assertThrows(InvalidFileFormatException.class, () -> bookService.upload(file));
    }

    @Test
    void shouldListBooksWhenTitleMatches() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").withAuthor("Author").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").withAuthor("Author").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search(wingsOfFire.getName(), "");

        assertEquals(1, books.size());
        assertEquals(wingsOfFire.getName(), books.get(0).getName());
    }

    @Test
    void shouldNotListBooksWhenTitleDoesNotMatches() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").withAuthor("Author").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").withAuthor("Author").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("Harry Potter", "");

        assertEquals(0, books.size());
    }

    @Test
    void shouldListBooksWhenAuthorMatches() {
        Book wingsOfFire = new BookTestBuilder().withAuthor("Wings of Fire").withName("Title").build();
        Book animalFarm = new BookTestBuilder().withAuthor("Animal Farm").withName("Title").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("", wingsOfFire.getAuthorName());

        assertEquals(1, books.size());
        assertEquals(wingsOfFire.getAuthorName(), books.get(0).getAuthorName());
    }

    @Test
    void shouldNotListBooksWhenAuthorDoesNotMatches() {
        Book wingsOfFire = new BookTestBuilder().withAuthor("Wings of Fire").withName("Title").build();
        Book animalFarm = new BookTestBuilder().withAuthor("Animal Farm").withName("Title").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("", "Harry Potter");

        assertEquals(0, books.size());
    }
}