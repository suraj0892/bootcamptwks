package com.tw.bootcamp.bookshop.book;

import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
public class SearchRequest {
    private final String title;
    private final String author;
}
