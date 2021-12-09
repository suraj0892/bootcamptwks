package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvBindByName;
import com.tw.bootcamp.bookshop.money.Money;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embedded;

@Getter
public class BookCsvBean {
    @CsvBindByName(column = "author")
    private String authorName;
    @CsvBindByName(column = "title")
    private String name;
    @CsvBindByName(column = "image_url")
    private String imageUrl;
    @CsvBindByName(column = "small_image_url")
    private String smallImageUrl;
    @CsvBindByName(column = "price")
    private double price;
    @CsvBindByName(column = "books_count")
    private String booksCount;
    @CsvBindByName(column = "isbn")
    private String isbn;
    @CsvBindByName(column = "isbn13")
    private String isbn13;
    @CsvBindByName(column = "original_publication_year")
    private int originalPublicationYear;
    @CsvBindByName(column = "original_title")
    private String originalTitle;
    @CsvBindByName(column = "language_code")
    private String languageCode;
    @CsvBindByName(column = "average_rating")
    private String averageRating;
}
