package com.novitskii.library.services;

import com.novitskii.library.models.Book;
import com.novitskii.library.models.Person;
import com.novitskii.library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final DataSource dataSource;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, DataSource dataSource) {
        this.peopleRepository = peopleRepository;
        this.dataSource = dataSource;
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    public List<Person> getAllPeople() {
        return peopleRepository.findAll();
    }

    public Person getPersonById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Person getPersonByName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    @Transactional
    public void deletePersonById(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void markTimeOuts(Person person) {
        Date currentDate = new Date();
        person.getBooks()
                .forEach(book ->
                        book.setExpired(TimeUnit.SECONDS
                                .convert(currentDate.getTime() - book.getTakenAt().getTime(),
                                        TimeUnit.MILLISECONDS) >= 30));
    }
}
