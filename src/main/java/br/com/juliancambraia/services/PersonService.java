package br.com.juliancambraia.services;

import br.com.juliancambraia.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public Person findById(String id) {
        logger.info("Finding on Person");

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Raimundo");
        person.setLastName("Nonato");
        person.setAddress("Rua Celestino Cavalcante, 101 - Uberaba, Minas Gerais - Brasil ");
        person.setGender("Male");
        return person;
    }

    public List<Person> findAll() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            personList.add(person);
        }
        logger.info("FindAll Persons");

        return personList;
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Person name " + i);
        person.setLastName("Last name " + i);
        person.setAddress("Some address in Brasil " + i);
        person.setGender("Male");
        return person;
    }

    public Person create(Person person) {
        logger.info("Create One Person");

        return person;
    }

    public Person update(Person person) {
        logger.info("Updated One Person");

        return person;
    }

    public void delete(String id) {
        logger.info("Delete One Person");
    }
}
