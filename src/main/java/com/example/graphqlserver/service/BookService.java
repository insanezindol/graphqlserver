package com.example.graphqlserver.service;

import com.example.graphqlserver.entity.Author;
import com.example.graphqlserver.entity.Book;
import com.example.graphqlserver.repository.AuthorRepository;
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
    private final AuthorRepository authorRepository;
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
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthorId(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByPriceRange(Double minPrice, Double maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchByKeyword(keyword);
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBookWithAuthor(Long id) {
        return bookRepository.findByIdWithAuthor(id);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksWithAuthors() {
        return bookRepository.findAllWithAuthor();
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthorName(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthorNationality(String nationality) {
        return bookRepository.findByAuthorNationality(nationality);
    }

    @Transactional
    public Book createBook(Book book) {
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new RuntimeException("Author ID is required");
        }

        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + book.getAuthor().getId()));

        book.setAuthor(author);

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
        if (bookDetails.getIsbn() != null) {
            book.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getDescription() != null) {
            book.setDescription(bookDetails.getDescription());
        }
        if (bookDetails.getPrice() != null) {
            book.setPrice(bookDetails.getPrice());
        }
        if (bookDetails.getPageCount() != null) {
            book.setPageCount(bookDetails.getPageCount());
        }
        if (bookDetails.getPublishedDate() != null) {
            book.setPublishedDate(parseDateTime(bookDetails.getPublishedDate().toString()));
        }
        if (bookDetails.getAuthor() != null && bookDetails.getAuthor().getId() != null) {
            Author author = authorRepository.findById(bookDetails.getAuthor().getId())
                    .orElseThrow(() -> new RuntimeException("Author not found with id: " + bookDetails.getAuthor().getId()));
            book.setAuthor(author);
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
