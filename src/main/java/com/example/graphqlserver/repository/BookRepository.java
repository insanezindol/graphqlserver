package com.example.graphqlserver.repository;

import com.example.graphqlserver.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findByPriceBetween(Double minPrice, Double maxPrice);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.author WHERE b.id = :id")
    Optional<Book> findByIdWithAuthor(@Param("id") Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.author")
    List<Book> findAllWithAuthor();

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.author WHERE b.author.nationality = :nationality")
    List<Book> findByAuthorNationality(@Param("nationality") String nationality);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.author a WHERE a.name = :authorName")
    List<Book> findByAuthorName(@Param("authorName") String authorName);
}
