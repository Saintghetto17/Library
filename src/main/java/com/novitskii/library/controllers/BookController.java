package com.novitskii.library.controllers;


import com.novitskii.library.models.Book;
import com.novitskii.library.services.BooksService;
import com.novitskii.library.services.PeopleService;
import com.novitskii.library.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/library/books")
public class BookController {
    private PeopleService peopleService;
    private BookValidator bookValidator;
    private BooksService booksService;

    @Autowired
    public BookController(PeopleService peopleService, BookValidator bookValidator, BooksService booksService) {
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
        this.booksService = booksService;
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("book") Book book) {
        return "book/search";
    }

    @PostMapping("/search")
    public String searchResults(@ModelAttribute("book") Book book, Model model) {
        String startLettersName = book.getName();
        List<Book> matchedBooks = booksService.getAllBooksStartingWith(startLettersName);
        model.addAttribute("books", matchedBooks);
        return "book/search";
    }

    @GetMapping("")
    public String listBooks(Model model) {
        model.addAttribute("books", booksService.getAllBooks());
        return "book/list";
    }

    @GetMapping(params = {"page", "books_per_page", "sort_by_year"})
    public String listBooksPerPage(@RequestParam("page") int page,
                                   @RequestParam("books_per_page") int bookPerPage,
                                   @RequestParam(name = "sort_by_year", required = false) boolean sortByYear,
                                   Model model) {
        if (!sortByYear) {
            model.addAttribute("books", booksService.getAllBooksOnPage(page, bookPerPage));
        } else {
            model.addAttribute("books", booksService.getAllBooksOnPageSorted(page, bookPerPage));
        }
        return "book/list";
    }

    @GetMapping(params = {"sort_by_year"})
    public String listAllBooksSortedByYear(@RequestParam("sort_by_year") boolean sortByYear, Model model) {
        if (sortByYear) {
            model.addAttribute("books", booksService.getAllBooksSortedByYear());
        } else {
            model.addAttribute("books", booksService.getAllBooks());
        }

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
        booksService.save(book);
        return "redirect:/library/books";
    }

    @GetMapping("/{id}/edit")
    public String updateBookGet(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.getBookById(id));
        return "book/edit";
    }

    @PostMapping("/{id}/edit")
    public String addPersonToBookPost(@RequestParam("personId") int personId, @PathVariable("id") int id) {
        booksService.update(id, personId);
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
        booksService.update(id, book);
        return "redirect:/library/books";
    }

    @GetMapping("/{id}")
    public String showBookGet(@PathVariable("id") int id, Model model) {
        Book book = booksService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.getAllPeople());
        model.addAttribute("booked_person", book.getPerson());
        return "book/show";
    }

    @PostMapping("/delete/{id}")
    public String releaseBookFromPerson(@PathVariable("id") int id, Model model) {
        Book book = booksService.getBookById(id);
        booksService.releaseFromPerson(id);
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.getAllPeople());
        model.addAttribute("booked_person", peopleService.getPersonById(book.getPerson().getId()));
        return "redirect:/library/books/{id}";
    }

    @PostMapping("/real_delete/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/library/books";
    }
}
