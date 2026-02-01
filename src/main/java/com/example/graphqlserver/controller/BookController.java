package com.example.graphqlserver.controller;


import com.example.graphqlserver.entity.Book;
import com.example.graphqlserver.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @QueryMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @QueryMapping
    public Optional<Book> getBookById(@Argument Long id) {
        return bookService.getBookById(id);
    }

    @QueryMapping
    public List<Book> getBooksByAuthor(@Argument String author) {
        return bookService.getBooksByAuthor(author);
    }

    @QueryMapping
    public List<Book> getBooksByTitle(@Argument String title) {
        return bookService.getBooksByTitle(title);
    }

    @QueryMapping
    public List<Book> searchBooks(@Argument String keyword) {
        return bookService.searchBooks(keyword);
    }

    @MutationMapping
    public Book createBook(@Argument(name = "bookInput") BookInput bookInput) {
        Book book = Book.builder()
                .title(bookInput.getTitle())
                .author(bookInput.getAuthor())
                .isbn(bookInput.getIsbn())
                .price(bookInput.getPrice())
                .publishedDate(bookInput.getPublishedDate() != null ?
                        LocalDateTime.parse(bookInput.getPublishedDate(), formatter) : null)
                .build();
        return bookService.createBook(book);
    }

    @MutationMapping
    public Book updateBook(@Argument(name = "bookUpdateInput") BookUpdateInput bookUpdateInput) {
        Book bookDetails = Book.builder()
                .title(bookUpdateInput.getTitle())
                .author(bookUpdateInput.getAuthor())
                .isbn(bookUpdateInput.getIsbn())
                .price(bookUpdateInput.getPrice())
                .publishedDate(bookUpdateInput.getPublishedDate() != null ?
                        LocalDateTime.parse(bookUpdateInput.getPublishedDate(), formatter) : null)
                .build();
        return bookService.updateBook(bookUpdateInput.getId(), bookDetails);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument Long id) {
        return bookService.deleteBook(id);
    }

    // Input 클래스들 (내부 클래스로 정의)
    public static class BookInput {
        private String title;
        private String author;
        private String isbn;
        private Double price;
        private String publishedDate;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public String getPublishedDate() { return publishedDate; }
        public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    }

    public static class BookUpdateInput {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Double price;
        private String publishedDate;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public String getPublishedDate() { return publishedDate; }
        public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    }

}
