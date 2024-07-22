package com.novitskii.library.services;

import com.novitskii.library.models.Book;
import com.novitskii.library.models.Person;
import com.novitskii.library.repositories.BooksRepository;
import com.novitskii.library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    private final PeopleRepository peopleRepository;


    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    public List<Book> getAllBooksOnPage(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Object getAllBooksOnPageSorted(int page, int bookPerPage) {
        return booksRepository.findAll(PageRequest.of(page, bookPerPage, Sort.by("year"))).getContent();
    }

    public List<Book> getAllBooksById(int id) {
        return booksRepository.findAllById(Collections.singletonList(id));
    }

    public List<Book> getAllBooksSortedByYear() {
        return booksRepository.findAll(Sort.by("year"));
    }

    public Book getBookByNameAndAuthorAndYear(String name, String author, int year) {
        return booksRepository.findByNameAndAuthorAndYear(name, author, year);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    public Book getBookById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(int id, Book bookUpdated) {
        bookUpdated.setId(id);
        booksRepository.save(bookUpdated);
    }

    @Transactional
    public void update(int id, int personId) {
        Book book = booksRepository.findById(id).orElse(null);
        if (book != null) {
            Person person = peopleRepository.findById(personId).orElse(null);
            if (person != null) {
                book.setPerson(person);
                book.setTakenAt(new Date());
                book.setExpired(false);
                booksRepository.save(book);
            }
        }
    }

    @Transactional
    public void releaseFromPerson(int id) {
        Book book = booksRepository.findById(id).orElse(null);
        if (book != null) {
            book.setPerson(null);
            book.setExpired(false);
            booksRepository.save(book);
        }
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> getAllBooksStartingWith(String startLettersName) {
        return booksRepository.findByNameStartingWith(startLettersName);
    }
}
