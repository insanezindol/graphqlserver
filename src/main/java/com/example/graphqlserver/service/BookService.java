package com.example.graphqlserver.service;

import com.example.graphqlserver.entity.Book;
import com.example.graphqlserver.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchByKeyword(keyword);
    }

    @Transactional
    public Book createBook(Book book) {
        if (book.getPublishedDate() != null) {
            book.setPublishedDate(parseDateTime(book.getPublishedDate().toString()));
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (bookDetails.getTitle() != null) {
            book.setTitle(bookDetails.getTitle());
        }
        if (bookDetails.getAuthor() != null) {
            book.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getIsbn() != null) {
            book.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getPrice() != null) {
            book.setPrice(bookDetails.getPrice());
        }
        if (bookDetails.getPublishedDate() != null) {
            book.setPublishedDate(parseDateTime(bookDetails.getPublishedDate().toString()));
        }

        return bookRepository.save(book);
    }

    @Transactional
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format. Please use ISO format (yyyy-MM-ddTHH:mm:ss)");
        }
    }

}
