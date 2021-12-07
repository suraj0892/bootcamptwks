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

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    private Validator validator;

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
                             .setIgnoreHeaderCase(true).setSkipHeaderRecord(true).setTrim(true).build());) {

            List<Book> books = convertInputCsvRecordToListOfBook(csvParser.getRecords());

            return bookRepository.saveAll(books);
        }
    }

    public List<Book> search(String title, String author) {

        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author)) {
            return bookRepository.findByNameAndAuthorNameStartingWith(title, author);
        }
        else if (StringUtils.isNotBlank(title)) {
            return bookRepository.findByNameStartingWith(title);
        }
        else if (StringUtils.isNotBlank(author)) {
            return bookRepository.findByAuthorNameStartingWith(author);
        }
        else {
            return new ArrayList<>();
        }
    }

    private List<Book> convertInputCsvRecordToListOfBook(List<CSVRecord> bookRecords){
        List<Book> books = new ArrayList<>();
        bookRecords.stream().forEach(bookRecord -> {
            books.add(Book.builder()
                    .name(bookRecord.get("title"))
                    .authorName(bookRecord.get("author"))
                    .isbn13(Long.parseLong(bookRecord.get("isbn13")))
                    .quantity(Integer.parseInt(bookRecord.get("books_count")))
                    .price(Money.rupees(Double.parseDouble(bookRecord.get("price")))).build());
        });
        return books;
    }

}
