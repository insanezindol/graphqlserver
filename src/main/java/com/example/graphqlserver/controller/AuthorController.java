package com.example.graphqlserver.controller;


import com.example.graphqlserver.dto.AuthorInput;
import com.example.graphqlserver.dto.AuthorUpdateInput;
import com.example.graphqlserver.entity.Author;
import com.example.graphqlserver.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 쿼리 매핑
    @QueryMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @QueryMapping
    public Optional<Author> getAuthorById(@Argument Long id) {
        return authorService.getAuthorById(id);
    }

    @QueryMapping
    public Optional<Author> getAuthorByEmail(@Argument String email) {
        return authorService.getAuthorByEmail(email);
    }

    @QueryMapping
    public List<Author> getAuthorsByName(@Argument String name) {
        return authorService.getAuthorsByName(name);
    }

    @QueryMapping
    public List<Author> getAuthorsByNationality(@Argument String nationality) {
        return authorService.getAuthorsByNationality(nationality);
    }

    @QueryMapping
    public Optional<Author> getAuthorWithBooks(@Argument Long id) {
        return authorService.getAuthorWithBooks(id);
    }

    @QueryMapping
    public List<Author> getAuthorsWithBooks() {
        return authorService.getAuthorsWithBooks();
    }

    // 뮤테이션 매핑
    @MutationMapping
    public Author createAuthor(@Argument(name = "authorInput") AuthorInput authorInput) {
        Author author = Author.builder()
                .name(authorInput.getName())
                .email(authorInput.getEmail())
                .bio(authorInput.getBio())
                .birthDate(authorInput.getBirthDate() != null ?
                        LocalDateTime.parse(authorInput.getBirthDate(), formatter) : null)
                .nationality(authorInput.getNationality())
                .build();
        return authorService.createAuthor(author);
    }

    @MutationMapping
    public Author updateAuthor(@Argument(name = "authorUpdateInput") AuthorUpdateInput authorUpdateInput) {
        Author authorDetails = Author.builder()
                .name(authorUpdateInput.getName())
                .email(authorUpdateInput.getEmail())
                .bio(authorUpdateInput.getBio())
                .birthDate(authorUpdateInput.getBirthDate() != null ?
                        LocalDateTime.parse(authorUpdateInput.getBirthDate(), formatter) : null)
                .nationality(authorUpdateInput.getNationality())
                .build();
        return authorService.updateAuthor(authorUpdateInput.getId(), authorDetails);
    }

    @MutationMapping
    public Boolean deleteAuthor(@Argument Long id) {
        return authorService.deleteAuthor(id);
    }

}
