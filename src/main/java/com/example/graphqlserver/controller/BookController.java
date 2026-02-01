package com.example.graphqlserver.controller;


import com.example.graphqlserver.dto.BookInput;
import com.example.graphqlserver.dto.BookUpdateInput;
import com.example.graphqlserver.entity.Author;
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

    // 쿼리 매핑
    @QueryMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @QueryMapping
    public Optional<Book> getBookById(@Argument Long id) {
        return bookService.getBookById(id);
    }

    @QueryMapping
    public List<Book> getBooksByTitle(@Argument String title) {
        return bookService.getBooksByTitle(title);
    }

    @QueryMapping
    public List<Book> getBooksByAuthorId(@Argument Long authorId) {
        return bookService.getBooksByAuthorId(authorId);
    }

    @QueryMapping
    public List<Book> getBooksByPriceRange(@Argument Float minPrice, @Argument Float maxPrice) {
        return bookService.getBooksByPriceRange(minPrice.doubleValue(), maxPrice.doubleValue());
    }

    @QueryMapping
    public List<Book> searchBooks(@Argument String keyword) {
        return bookService.searchBooks(keyword);
    }

    @QueryMapping
    public Optional<Book> getBookWithAuthor(@Argument Long id) {
        return bookService.getBookWithAuthor(id);
    }

    @QueryMapping
    public List<Book> getBooksWithAuthors() {
        return bookService.getBooksWithAuthors();
    }

    @QueryMapping
    public List<Book> getBooksByAuthorName(@Argument String authorName) {
        return bookService.getBooksByAuthorName(authorName);
    }

    @QueryMapping
    public List<Book> getBooksByAuthorNationality(@Argument String nationality) {
        return bookService.getBooksByAuthorNationality(nationality);
    }

    // 뮤테이션 매핑
    @MutationMapping
    public Book createBook(@Argument(name = "bookInput") BookInput bookInput) {
        Author author = new Author();
        author.setId(bookInput.getAuthorId());

        Book book = Book.builder()
                .title(bookInput.getTitle())
                .isbn(bookInput.getIsbn())
                .description(bookInput.getDescription())
                .price(bookInput.getPrice())
                .pageCount(bookInput.getPageCount())
                .publishedDate(bookInput.getPublishedDate() != null ?
                        LocalDateTime.parse(bookInput.getPublishedDate(), formatter) : null)
                .author(author)
                .build();
        return bookService.createBook(book);
    }

    @MutationMapping
    public Book updateBook(@Argument(name = "bookUpdateInput") BookUpdateInput bookUpdateInput) {
        Author author = null;
        if (bookUpdateInput.getAuthorId() != null) {
            author = new Author();
            author.setId(bookUpdateInput.getAuthorId());
        }

        Book bookDetails = Book.builder()
                .title(bookUpdateInput.getTitle())
                .isbn(bookUpdateInput.getIsbn())
                .description(bookUpdateInput.getDescription())
                .price(bookUpdateInput.getPrice())
                .pageCount(bookUpdateInput.getPageCount())
                .publishedDate(bookUpdateInput.getPublishedDate() != null ?
                        LocalDateTime.parse(bookUpdateInput.getPublishedDate(), formatter) : null)
                .author(author)
                .build();
        return bookService.updateBook(bookUpdateInput.getId(), bookDetails);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument Long id) {
        return bookService.deleteBook(id);
    }

}
