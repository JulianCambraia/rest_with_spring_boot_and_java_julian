package br.com.juliancambraia.services;

import br.com.juliancambraia.config.ModelMapperConfig;
import br.com.juliancambraia.controller.PersonController;
import br.com.juliancambraia.data.vo.v1.PersonVO;
import br.com.juliancambraia.data.vo.v2.PersonVOV2;
import br.com.juliancambraia.exceptions.RequiredObjectIsNullException;
import br.com.juliancambraia.exceptions.ResourceNotFoundException;
import br.com.juliancambraia.mapper.custom.PersonMapper;
import br.com.juliancambraia.model.Person;
import br.com.juliancambraia.repository.PersonRepository;
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
    ModelMapperConfig mapper;
    PersonMapper personMapper;

    @Autowired
    public PersonService(PersonRepository repository, ModelMapperConfig mapper, PersonMapper personMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.personMapper = personMapper;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding on Person");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = mapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return vo;
    }

    public List<PersonVO> findAll() {
        logger.info("FindAll Persons");
        var persons = repository.findAll();
        var lists = new ArrayList<PersonVO>();
        persons.forEach(person -> {
            mapper = new ModelMapperConfig();
            var vo = mapper.parseObject(person, PersonVO.class);
            vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
            lists.add(vo);
        });
        return lists;
    }

    public PersonVO create(PersonVO person) {
        logger.info("Create One Person");
        if (person == null) throw new RequiredObjectIsNullException();
        mapper = new ModelMapperConfig();
        var entity = mapper.parseObject(person, Person.class);
        var vo = mapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());

        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updated One Person");

        if (person == null) throw new RequiredObjectIsNullException();
        mapper = new ModelMapperConfig();
        Person entity = mapper.parseObject(findById(person.getId()), Person.class);
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var result = repository.save(entity);
        var vo = mapper.parseObject(result, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Delete One Person");
        repository.deleteById(id);
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Create One Person");
        if (person == null) throw new RequiredObjectIsNullException();

        var entity = personMapper.convertVoToEntity(person);
        return personMapper.convertEntityToVo(repository.save(entity));
    }
}
