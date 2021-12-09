package com.tw.bootcamp.bookshop.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tw.bootcamp.bookshop.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "books")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String authorName;

    private Integer quantity;

    private Long isbn13;

    @Embedded
    private Money price;

    private String imageUrl;
    private String smallImageUrl;
    private String isbn;
    private int originalPublicationYear;
    private String originalTitle;
    private String languageCode;
    private String averageRating;

    public Book(String name, String authorName, Integer quantity, Long isbn13, Money price, String imageUrl,
                String smallImageUrl, String isbn, int originalPublicationYear, String originalTitle, String languageCode, String averageRating) {
        this.name = name;
        this.authorName = authorName;
        this.quantity = quantity;
        this.isbn13 = isbn13;
        this.price = price;
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
        this.isbn = isbn;
        this.originalPublicationYear = originalPublicationYear;
        this.originalTitle = originalTitle;
        this.languageCode = languageCode;
        this.averageRating = averageRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn13.equals(book.isbn13);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn13);
    }

    public void update(Book oldBook) {
        this.id = oldBook.id;
        this.quantity = oldBook.quantity + this.quantity;
    }
}
