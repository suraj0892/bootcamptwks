package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.tw.bootcamp.bookshop.money.Money;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookCsvReader {
    static List<Book> readFromFile(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream());){
            ColumnPositionMappingStrategy ms = new ColumnPositionMappingStrategy();
            ms.setType(BookCsvBean.class);
            CsvToBean<BookCsvBean> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(BookCsvBean.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<BookCsvBean> bookCsvBeans = csvToBean.parse();
            List<Book> booksList = mapCSVBookToBookEntity(bookCsvBeans);
            return booksList;
        }
    }

    private static List<Book> mapCSVBookToBookEntity(List<BookCsvBean> bookCsvBeans) {
        List<Book> bookList = bookCsvBeans.stream()
                .map(csvBook -> new Book(csvBook.getName(), csvBook.getAuthorName(), Integer.parseInt(csvBook.getBooksCount()), Long.parseLong(csvBook.getIsbn13()),Money.rupees(csvBook.getPrice()),
                        csvBook.getImageUrl(), csvBook.getSmallImageUrl(), csvBook.getIsbn(), csvBook.getOriginalPublicationYear(), csvBook.getOriginalTitle(),
                        csvBook.getLanguageCode(), csvBook.getAverageRating()))
                .collect(Collectors.toList());
        return bookList;
    }

}
