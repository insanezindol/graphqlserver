package com.example.graphqlserver.service;

import com.example.graphqlserver.entity.Author;
import com.example.graphqlserver.repository.AuthorRepository;
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
public class AuthorService {

    private final AuthorRepository authorRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Author> getAuthorByEmail(String email) {
        return authorRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Author> getAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Author> getAuthorsByNationality(String nationality) {
        return authorRepository.findByNationality(nationality);
    }

    @Transactional(readOnly = true)
    public Optional<Author> getAuthorWithBooks(Long id) {
        return authorRepository.findByIdWithBooks(id);
    }

    @Transactional(readOnly = true)
    public List<Author> getAuthorsWithBooks() {
        return authorRepository.findAllWithBooks();
    }

    @Transactional
    public Author createAuthor(Author author) {
        if (author.getBirthDate() != null) {
            author.setBirthDate(parseDateTime(author.getBirthDate().toString()));
        }
        return authorRepository.save(author);
    }

    @Transactional
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        if (authorDetails.getName() != null) {
            author.setName(authorDetails.getName());
        }
        if (authorDetails.getEmail() != null) {
            author.setEmail(authorDetails.getEmail());
        }
        if (authorDetails.getBio() != null) {
            author.setBio(authorDetails.getBio());
        }
        if (authorDetails.getBirthDate() != null) {
            author.setBirthDate(parseDateTime(authorDetails.getBirthDate().toString()));
        }
        if (authorDetails.getNationality() != null) {
            author.setNationality(authorDetails.getNationality());
        }

        return authorRepository.save(author);
    }

    @Transactional
    public boolean deleteAuthor(Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
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
