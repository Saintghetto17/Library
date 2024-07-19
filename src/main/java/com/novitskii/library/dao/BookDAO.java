package com.novitskii.library.dao;

import com.novitskii.library.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream().toList();
    }

    public List<Book> list() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Book> show(String name, String author, int year) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE name=? AND author=? AND year=?",
                        new Object[]{name, author, year},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream()
                .findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(name, author, year) VALUES(?,?,?)",
                book.getName(),
                book.getAuthor(),
                book.getYear());
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream()
                .findAny().orElse(null);
    }


    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET name=?, author=?, year=? WHERE id=?",
                book.getName(),
                book.getAuthor(),
                book.getYear(),
                id);
    }

    public void update(int id, int personId) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?",
                personId,
                id);
    }

    public void releaseFromPerson(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?",
                null,
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }
}
