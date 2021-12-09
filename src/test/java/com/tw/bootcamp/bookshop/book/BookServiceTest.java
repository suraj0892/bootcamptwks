package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.book.exceptions.InvalidFileFormatException;
import com.tw.bootcamp.bookshop.book.exceptions.InvalidRequestParameterException;
import com.tw.bootcamp.bookshop.book.exceptions.NoBooksFoundException;
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

    @Test
    void shouldFetchAllBooksBeSortedByPriceAscending() {
        Book lowerPriceBook = new BookTestBuilder().withPrice(400)
                .build();
        Book higherPriceBook = new BookTestBuilder().withPrice(750)
                .build();
        bookRepository.save(higherPriceBook);
        bookRepository.save(lowerPriceBook);

        List<Book> books = bookService.fetchAllByOrder("asc");

        assertEquals(2, books.size());
        assertEquals(400, books.get(0)
                .getPrice().getAmount());
    }

    @Test
    void shouldFetchAllBooksBeSortedByPriceDescending() {
        Book lowerPriceBook = new BookTestBuilder().withPrice(400)
                .build();
        Book higherPriceBook = new BookTestBuilder().withPrice(750)
                .build();
        bookRepository.save(lowerPriceBook);
        bookRepository.save(higherPriceBook);

        List<Book> books = bookService.fetchAllByOrder("desc");

        assertEquals(2, books.size());
        assertEquals(750, books.get(0)
                .getPrice().getAmount());
    }

    @Nested
    public class load {
        @Test
        void ShouldBeAbleToLoadBooksWithNoMatchingIsbn() throws IOException, InvalidFileFormatException {
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
            MockMultipartFile bookNameUpdated = new MockMultipartFile("file", "bookNameUpdated.csv", "text/csv",
                    classloader.getResourceAsStream("bookNameUpdated.csv"));

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

    @Nested
    public class search {
        @Test
        void shouldListBooksWhenTitleMatches() throws InvalidRequestParameterException, NoBooksFoundException {
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
        void shouldNotListBooksWhenTitleDoesNotMatches() throws InvalidRequestParameterException, NoBooksFoundException {
            Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire")
                    .withAuthor("Author")
                    .build();
            Book animalFarm = new BookTestBuilder().withName("Animal Farm")
                    .withAuthor("Author")
                    .build();
            bookRepository.save(wingsOfFire);
            bookRepository.save(animalFarm);

            assertThrows(NoBooksFoundException.class, () -> bookService.search("Harry Potter", ""));
        }

        @Test
        void shouldListBooksWhenAuthorMatches() throws InvalidRequestParameterException, NoBooksFoundException {
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
        void shouldNotListBooksWhenAuthorDoesNotMatches() throws InvalidRequestParameterException, NoBooksFoundException {
            Book wingsOfFire = new BookTestBuilder().withAuthor("Wings of Fire")
                    .withName("Title")
                    .build();
            Book animalFarm = new BookTestBuilder().withAuthor("Animal Farm")
                    .withName("Title")
                    .build();
            bookRepository.save(wingsOfFire);
            bookRepository.save(animalFarm);

            assertThrows(NoBooksFoundException.class, () -> bookService.search("", "Harry Potter"));
        }

        @Test
        void shouldListBooksWhenAuthorAndTitleMatches() throws InvalidRequestParameterException, NoBooksFoundException {
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
        void shouldNotListBooksWhenAuthorAndTitleDoesNotMatches() throws InvalidRequestParameterException, NoBooksFoundException {
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

            assertThrows(NoBooksFoundException.class, () -> bookService.search("Extreme Programming", "Kent Beck"));
        }

        @Test
        void shouldThrowExceptionWhenTitleAndAuthorAreNullOrEmpty() {
            assertThrows(InvalidRequestParameterException.class, () -> bookService.search("", ""));
        }

        @Test
        void shouldListBooksWhenAuthorAndTitleMatchesIgnoringCase() throws InvalidRequestParameterException, NoBooksFoundException {
            Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").withAuthor("Saugata").build();
            Book animalFarm = new BookTestBuilder().withName("Animal Farm").withAuthor("Ankit").build();
            Book wingsOfFireII = new BookTestBuilder().withName("Wings of Fire II").withAuthor("SaugataB").build();
            Book animalFarmII = new BookTestBuilder().withName("Animal Farm II").withAuthor("AnkitJ").build();
            bookRepository.save(wingsOfFire);
            bookRepository.save(animalFarm);
            bookRepository.save(wingsOfFireII);
            bookRepository.save(animalFarmII);

            List<Book> books = bookService.search("animal farm", "ANKIT");

            assertEquals(2, books.size());
            assertTrue(books.stream()
                    .allMatch(book -> book.getAuthorName()
                            .startsWith("Ankit")));
            assertTrue(books.stream()
                    .allMatch(book -> book.getName()
                            .startsWith("Animal Farm")));
        }
    }
}