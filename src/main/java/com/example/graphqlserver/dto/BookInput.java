package com.example.graphqlserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookInput {
    private String title;
    private String isbn;
    private String description;
    private Double price;
    private String publishedDate;
    private Integer pageCount;
    private Long authorId;
}
