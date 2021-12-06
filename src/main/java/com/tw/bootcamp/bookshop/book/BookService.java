package com.tw.bootcamp.bookshop.book;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
}
