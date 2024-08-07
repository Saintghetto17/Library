package com.novitskii.library.repositories;

import com.novitskii.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    Book findByNameAndAuthorAndYear(String name, String author, int year);

    List<Book> findByNameStartingWith(String startLettersName);
}
