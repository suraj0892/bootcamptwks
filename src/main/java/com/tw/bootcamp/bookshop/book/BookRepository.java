package com.tw.bootcamp.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>  {
    List<Book> findAllByOrderByNameAsc();

    List<Book> findByNameStartingWithAndAuthorNameStartingWith(String title, String author);

    @Query("Select b from Book b where b.isbn13 in (:isbns)")
    List<Book> findAllByIsbn(@Param("isbns") List<Long> newBooksIsbn);
}
