package com.novitskii.library.controllers;

import com.novitskii.library.dao.BookDAO;
import com.novitskii.library.dao.PersonDAO;
import com.novitskii.library.models.Person;
import com.novitskii.library.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/library/people")
public class PersonVisitorController {


    private PersonDAO personDAO;
    private BookDAO bookDAO;
    private PersonValidator personValidator;

    @Autowired
    public PersonVisitorController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.personValidator = personValidator;
    }

    @GetMapping("")
    public String listPeople(Model model) {
        model.addAttribute("people", personDAO.list());
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
        personDAO.save(person);
        return "redirect:/library/people";
    }

    @GetMapping("/{id}/edit")
    public String editPersonGet(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
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
        personDAO.update(id, person);
        return "redirect:/library/people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("books", bookDAO.getBooks(id));
        return "people/show";
    }

    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        System.out.println(id);
        personDAO.delete(id);
        return "redirect:/library/people";
    }
}
