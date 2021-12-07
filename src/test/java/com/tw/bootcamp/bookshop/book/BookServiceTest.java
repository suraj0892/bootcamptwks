package com.tw.bootcamp.bookshop.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
        Book book = new BookTestBuilder().withName("title")
                .build();
        bookRepository.save(book);

        List<Book> books = bookService.fetchAll();

        assertEquals(1, books.size());
        assertEquals("title", books.get(0)
                .getName());
    }

    @Test
    void shouldFetchAllBooksBeSortedByNameAscending() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire")
                .build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm")
                .build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.fetchAll();

        assertEquals(2, books.size());
        assertEquals("Animal Farm", books.get(0)
                .getName());
    }

    @Nested
    public class loadBooks {
        @Test
        void ShouldBeAbleToLoadBooks() throws IOException, InvalidFileFormatException {
            ClassLoader classloader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream bookList = classloader.getResourceAsStream("test.csv");
            MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", bookList);

            List<Book> books = bookService.upload(file);

            assertEquals(1, books.size());
            assertEquals(books.get(0)
                    .getAuthorName(), "Cassandra Clare");
        }

        @Test
        void ShouldNotBeAbleToLoadWhenThereIsValidationError() throws IOException {
            ClassLoader classloader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream bookList = classloader.getResourceAsStream("error.csv");
            MockMultipartFile file = new MockMultipartFile("file", "error.csv", "text/csv", bookList);

            assertThrows(Exception.class, () -> bookService.upload(file));
        }

        @Test
        void ShouldNotBeAbleToLoadWhenThereIsMissingBookCount() throws IOException {
            ClassLoader classloader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream bookList = classloader.getResourceAsStream("errorWithMissingBookCount.csv");
            MockMultipartFile file = new MockMultipartFile("file", "errorWithMissingBookCount.csv", "text/csv", bookList);

            assertThrows(NumberFormatException.class, () -> bookService.upload(file));
        }

        @Test
        void shouldBeAbleToUploadBookwithMatchingIsbn() throws InvalidFileFormatException, IOException {

            ClassLoader classloader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream bookList = classloader.getResourceAsStream("test.csv");
            MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", bookList);

            List<Book> books = bookService.upload(file);
            assertEquals(1, books.size());
            books = bookService.upload(file);
            assertEquals(1, books.size());
            assertEquals(20, books.get(0)
                    .getQuantity());

        }

        @Test
        void shouldBeAbleToUploadBookwithMatchingIsbnWithNameUpdated() throws InvalidFileFormatException, IOException {

            ClassLoader classloader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream bookList = classloader.getResourceAsStream("test.csv");
            MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", bookList);
            MockMultipartFile bookNameUpdated = new MockMultipartFile("file", "bookNameUpdated.csv", "text/csv", classloader.getResourceAsStream("bookNameUpdated.csv"));

            List<Book> books = bookService.upload(file);
            List<Book> updatedBook = bookService.upload(bookNameUpdated);
            assertEquals("Updated City of Bones", updatedBook.get(0).getName());

        }

        @Test
        void ShouldNotBeAbleToLoadWhenInputFileIsNotCsv() throws IOException, InvalidFileFormatException {
            ClassLoader classloader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream bookList = classloader.getResourceAsStream("error.txt");
            MockMultipartFile file = new MockMultipartFile("file", "error.txt", "plain/txt", bookList);

            assertThrows(InvalidFileFormatException.class, () -> bookService.upload(file));
        }
    }

    @Test
    void shouldListBooksWhenTitleMatches() throws InvalidRequestParameterException {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire")
                .withAuthor("Author")
                .build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm")
                .withAuthor("Author")
                .build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search(wingsOfFire.getName(), "");

        assertEquals(1, books.size());
        assertEquals(wingsOfFire.getName(), books.get(0)
                .getName());
    }

    @Test
    void shouldNotListBooksWhenTitleDoesNotMatches() throws InvalidRequestParameterException {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire")
                .withAuthor("Author")
                .build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm")
                .withAuthor("Author")
                .build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("Harry Potter", "");

        assertEquals(0, books.size());
    }

    @Test
    void shouldListBooksWhenAuthorMatches() throws InvalidRequestParameterException {
        Book wingsOfFire = new BookTestBuilder().withAuthor("Wings of Fire")
                .withName("Title")
                .build();
        Book animalFarm = new BookTestBuilder().withAuthor("Animal Farm")
                .withName("Title")
                .build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("", wingsOfFire.getAuthorName());

        assertEquals(1, books.size());
        assertEquals(wingsOfFire.getAuthorName(), books.get(0)
                .getAuthorName());
    }

    @Test
    void shouldNotListBooksWhenAuthorDoesNotMatches() throws InvalidRequestParameterException {
        Book wingsOfFire = new BookTestBuilder().withAuthor("Wings of Fire")
                .withName("Title")
                .build();
        Book animalFarm = new BookTestBuilder().withAuthor("Animal Farm")
                .withName("Title")
                .build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("", "Harry Potter");

        assertEquals(0, books.size());
    }

    @Test
    void shouldListBooksWhenAuthorAndTitleMatches() throws InvalidRequestParameterException {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire")
                .withAuthor("Saugata")
                .build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm")
                .withAuthor("Ankit")
                .build();
        Book wingsOfFireII = new BookTestBuilder().withName("Wings of Fire II")
                .withAuthor("SaugataB")
                .build();
        Book animalFarmII = new BookTestBuilder().withName("Animal Farm II")
                .withAuthor("AnkitJ")
                .build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);
        bookRepository.save(wingsOfFireII);
        bookRepository.save(animalFarmII);

        List<Book> books = bookService.search("Animal Farm", "Ankit");

        assertEquals(2, books.size());
        assertTrue(books.stream()
                .allMatch(book -> book.getAuthorName()
                        .startsWith("Ankit")));
        assertTrue(books.stream()
                .allMatch(book -> book.getName()
                        .startsWith("Animal Farm")));
    }

    @Test
    void shouldListBooksWhenAuthorAndTitleDoesNotMatches() throws InvalidRequestParameterException {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire")
                .withAuthor("Saugata")
                .build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm")
                .withAuthor("Ankit")
                .build();
        Book wingsOfFireII = new BookTestBuilder().withName("Wings of Fire II")
                .withAuthor("SaugataB")
                .build();
        Book animalFarmII = new BookTestBuilder().withName("Animal Farm II")
                .withAuthor("AnkitJ")
                .build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);
        bookRepository.save(wingsOfFireII);
        bookRepository.save(animalFarmII);

        List<Book> books = bookService.search("Extreme Programming", "Kent Beck");

        assertEquals(0, books.size());
    }

    @Test
    void shouldThrowExceptionWhenTitleAndAuthorAreNullOrEmpty() {
        assertThrows(InvalidRequestParameterException.class, () -> bookService.search("", ""));
    }
}