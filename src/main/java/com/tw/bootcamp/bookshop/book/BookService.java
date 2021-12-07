package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> fetchAll() {
        return bookRepository.findAllByOrderByNameAsc();
    }

    public List<Book> upload(@RequestParam("file") MultipartFile file) throws IOException, InvalidFileFormatException {

        if(!file.getContentType().equalsIgnoreCase("text/csv"))
            throw new InvalidFileFormatException("Only Valid csv files are allowed");

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader()
                             .setIgnoreHeaderCase(true).setSkipHeaderRecord(true).setTrim(true).build())) {

            List<Book> newBooks = convertInputCsvRecordToListOfBook(csvParser.getRecords());

            updateBookForMatchingIsbn(newBooks);

            return bookRepository.saveAll(newBooks);
        }
    }

    private void updateBookForMatchingIsbn(List<Book> newBooks) {
        List<Long> newBooksIsbn = newBooks.stream().map(Book::getIsbn13).collect(Collectors.toList());
        List<Book> oldBooksMatchingIsbn = bookRepository.findAllByIsbn(newBooksIsbn);
        for(Book newBook: newBooks){
            if(oldBooksMatchingIsbn.contains(newBook)){
                Book oldBook = getMatchingBook(oldBooksMatchingIsbn, newBook);
                newBook.update(oldBook);
            }
        }
    }

    private Book getMatchingBook(List<Book> oldBooksMatchingIsbn, Book newBook) {
        return oldBooksMatchingIsbn.stream()
                .filter(book1 -> book1.equals(newBook))
                .findFirst().get();
    }

    public List<Book> search(String title, String author) throws InvalidRequestParameterException, NoBooksFoundException {
        if (StringUtils.isBlank(title) && StringUtils.isBlank(author)) {
            throw new InvalidRequestParameterException("Book title and author should not be empty!");
        }

        List<Book> foundBooks = bookRepository.
                findByNameStartingWithIgnoreCaseAndAuthorNameStartingWithIgnoreCase(
                        Optional.ofNullable(title).orElse(""),
                        Optional.ofNullable(author).orElse(""));
        if(foundBooks.size() <=0){
            throw new NoBooksFoundException("No Match Found!");
        }
        return foundBooks;
    }

    private List<Book> convertInputCsvRecordToListOfBook(List<CSVRecord> bookRecords){
        List<Book> books = new ArrayList<>();
        bookRecords.forEach(bookRecord -> books.add(Book.builder()
                .name(bookRecord.get("title"))
                .authorName(bookRecord.get("author"))
                .isbn13(Long.parseLong(bookRecord.get("isbn13")))
                .quantity(Integer.parseInt(bookRecord.get("books_count")))
                .price(Money.rupees(Double.parseDouble(bookRecord.get("price")))).build()));
        return books;
    }
}
