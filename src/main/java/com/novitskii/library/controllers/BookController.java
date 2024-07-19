package com.novitskii.library.controllers;


import com.novitskii.library.dao.BookDAO;
import com.novitskii.library.dao.PersonDAO;
import com.novitskii.library.models.Book;
import com.novitskii.library.models.Person;
import com.novitskii.library.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/library/books")
public class BookController {
    private BookDAO bookDAO;
    private BookValidator bookValidator;
    private PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, BookValidator bookValidator, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.bookValidator = bookValidator;
        this.personDAO = personDAO;
    }

    @GetMapping("")
    public String listBooks(Model model) {
        model.addAttribute("books", bookDAO.list());
        return "book/list";
    }


    @GetMapping("/new")
    public String addNewBookGet(@ModelAttribute("book") Book book) {
        return "book/add";
    }

    @PostMapping("")
    public String addNewBookPost(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "book/add";
        }
        bookDAO.save(book);
        return "redirect:/library/books";
    }

    @GetMapping("/{id}/edit")
    public String updateBookGet(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.show(id));
        return "book/edit";
    }

    @PostMapping("/{id}/edit")
    public String addPersonToBookPost(@RequestParam("personId") int personId, @PathVariable("id") int id) {
        bookDAO.update(id, personId);
        return "redirect:/library/books/{id}";
    }

    @PostMapping("/{id}")
    public String editBookPost(@ModelAttribute("book") @Valid Book book,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        bookDAO.update(id, book);
        return "redirect:/library/books";
    }

    @GetMapping("/{id}")
    public String showBookGet(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.show(id);
        model.addAttribute("book", book);
        model.addAttribute("people", personDAO.list());
        model.addAttribute("booked_person", personDAO.show(book.getPersonId()));
        return "book/show";
    }

    @PostMapping("/delete/{id}")
    public String releaseBookFromPerson(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.show(id);
        bookDAO.releaseFromPerson(id);
        model.addAttribute("book", book);
        model.addAttribute("people", personDAO.list());
        model.addAttribute("booked_person", personDAO.show(book.getPersonId()));
        return "redirect:/library/books/{id}";
    }

    @PostMapping("/real_delete/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/library/books";
    }
}
