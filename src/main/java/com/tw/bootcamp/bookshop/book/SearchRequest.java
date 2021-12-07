package com.tw.bootcamp.bookshop.book;

import lombok.*;

@AllArgsConstructor
@Getter
public class SearchRequest {
    private final String title;
    private final String author;
}
