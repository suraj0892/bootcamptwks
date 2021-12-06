package com.tw.bootcamp.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>  {
    List<Book> findAllByOrderByNameAsc();

    List<Book> findByNameStartingWith(String title);

    List<Book> findByAuthorNameStartingWith(String author);

    List<Book> findByNameAndAuthorNameStartingWith(String title, String author);
}
