package com.novitskii.library.controllers;

import com.novitskii.library.models.Book;
import com.novitskii.library.models.Person;
import com.novitskii.library.services.BooksService;
import com.novitskii.library.services.PeopleService;
import com.novitskii.library.util.PersonValidator;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/library/people")
public class PersonVisitorController {


    private PeopleService peopleService;
    private BooksService booksService;
    private PersonValidator personValidator;
    private final EntityManager entityManager;

    @Autowired
    public PersonVisitorController(PeopleService peopleService,
                                   BooksService booksService,
                                   PersonValidator personValidator,
                                   EntityManager entityManager) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personValidator = personValidator;
        this.entityManager = entityManager;
    }

    @GetMapping("")
    public String listPeople(Model model) {
        model.addAttribute("people", peopleService.getAllPeople());
        return "people/list";
    }

    @GetMapping("/new")
    public String addPersonGet(@ModelAttribute("person") Person person) {
        return "people/add";
    }

    @PostMapping("")
    public String addPersonPost(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/add";
        }
        peopleService.save(person);
        return "redirect:/library/people";
    }

    @GetMapping("/{id}/edit")
    public String editPersonGet(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.getPersonById(id));
        return "people/edit";
    }

    @PostMapping("/{id}")
    public String editPersonPatch(@ModelAttribute("person") @Valid Person person,
                                  BindingResult bindingResult,
                                  @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.update(id, person);
        return "redirect:/library/people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Person person = peopleService.getPersonById(id);
        model.addAttribute("person", person);
        peopleService.markTimeOuts(person);
        model.addAttribute("books", person.getBooks());
        return "people/show";
    }

    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        System.out.println(id);
        peopleService.deletePersonById(id);
        return "redirect:/library/people";
    }
}
