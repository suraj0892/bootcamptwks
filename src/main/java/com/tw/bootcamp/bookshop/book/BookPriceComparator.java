package com.tw.bootcamp.bookshop.book;

import java.util.Comparator;

public class BookPriceComparator implements Comparator<Book> {

    @Override
    public int compare(Book firstBook, Book secondBook) {
        return (int) (firstBook.getPrice().getAmount() - (secondBook.getPrice().getAmount()));
    }
}