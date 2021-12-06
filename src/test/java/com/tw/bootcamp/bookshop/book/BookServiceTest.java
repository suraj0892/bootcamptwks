package com.tw.bootcamp.bookshop.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    void shouldListBooksWhenTitleMatches() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").withAuthor("").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").withAuthor("").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search(wingsOfFire.getName(), wingsOfFire.getAuthorName());

        assertEquals(1, books.size());
        assertEquals(wingsOfFire.getName(), books.get(0).getName());
    }

    @Test
    void shouldNotListBooksWhenTitleDoesNotMatches() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").withAuthor("").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").withAuthor("").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("Harry Potter", wingsOfFire.getAuthorName());

        assertEquals(0, books.size());
    }

    @Test
    void shouldListBooksWhenAuthorMatches() {
        Book wingsOfFire = new BookTestBuilder().withAuthor("Wings of Fire").withName("").build();
        Book animalFarm = new BookTestBuilder().withAuthor("Animal Farm").withName("").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search(wingsOfFire.getName(), wingsOfFire.getAuthorName());

        assertEquals(1, books.size());
        assertEquals(wingsOfFire.getAuthorName(), books.get(0).getAuthorName());
    }

    @Test
    void shouldNotListBooksWhenAuthorDoesNotMatches() {
        Book wingsOfFire = new BookTestBuilder().withAuthor("Wings of Fire").withName("").build();
        Book animalFarm = new BookTestBuilder().withAuthor("Animal Farm").withName("").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search(wingsOfFire.getName(), "Harry Potter");

        assertEquals(0, books.size());
    }
}