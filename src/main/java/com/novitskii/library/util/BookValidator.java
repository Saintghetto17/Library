package com.novitskii.library.util;

import com.novitskii.library.dao.BookDAO;
import com.novitskii.library.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    private BookDAO bookDAO;

    @Autowired
    public BookValidator(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
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
        if (bookDAO.show(book.getName(), book.getAuthor(), book.getYear()).isPresent()) {
            errors.reject("global", "This book by this author is already exists");
        }
    }
}
