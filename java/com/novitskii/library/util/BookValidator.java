package com.novitskii.library.util;

import com.novitskii.library.models.Book;
import com.novitskii.library.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    private BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;
        if (book.getYear() <= 0) {
            errors.reject("year", "Year must be greater than zero");
        }
        if (booksService.getBookByNameAndAuthorAndYear(book.getName(), book.getAuthor(), book.getYear()) != null) {
            errors.reject("global", "This book by this author is already exists");
        }
    }
}
