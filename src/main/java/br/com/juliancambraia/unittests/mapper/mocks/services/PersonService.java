package br.com.juliancambraia.unittests.mapper.mocks.services;

import br.com.juliancambraia.controller.PersonController;
import br.com.juliancambraia.data.vo.v1.PersonVO;
import br.com.juliancambraia.exceptions.RequiredObjectIsNullException;
import br.com.juliancambraia.exceptions.ResourceNotFoundException;
import br.com.juliancambraia.model.Person;
import br.com.juliancambraia.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    Logger logger = Logger.getLogger(PersonService.class.getName());

    PersonRepository repository;

    @Autowired
    ModelMapper mapper;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding on Person");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = mapper.map(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return vo;
    }

    public List<PersonVO> findAll() {
        logger.info("FindAll Persons");
        var persons = repository.findAll();
        var lists = new ArrayList<PersonVO>();
        persons.forEach(person -> {
            var vo = mapper.map(person, PersonVO.class);
            vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
            lists.add(vo);
        });
        return lists;
    }

    public PersonVO create(PersonVO person) {
        logger.info("Create One Person");
        if (person == null) throw new RequiredObjectIsNullException();

        var entity = mapper.map(person, Person.class);
        var vo = mapper.map(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());

        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updated One Person");

        if (person == null) throw new RequiredObjectIsNullException();

        Person entity = mapper.map(findById(person.getId()), Person.class);
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var result = repository.save(entity);
        var vo = mapper.map(result, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Delete One Person");
        repository.deleteById(id);
    }
}
