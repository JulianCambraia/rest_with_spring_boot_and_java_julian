package br.com.juliancambraia.services;

import br.com.juliancambraia.controller.PersonController;
import br.com.juliancambraia.data.vo.v1.PersonVO;
import br.com.juliancambraia.data.vo.v2.PersonVOV2;
import br.com.juliancambraia.exceptions.ResourceNotFoundException;
import br.com.juliancambraia.mapper.DozerMapper;
import br.com.juliancambraia.mapper.custom.PersonMapper;
import br.com.juliancambraia.model.Person;
import br.com.juliancambraia.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    private final PersonRepository repository;

    final
    PersonMapper mapper;

    public PersonService(PersonRepository repository, PersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding on Person");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public List<PersonVO> findAll() {
        logger.info("FindAll Persons");
        return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
    }

    public PersonVO create(PersonVO person) {
        logger.info("Create One Person");
        var entity = DozerMapper.parseObject(person, Person.class);
        return DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    }

    public PersonVOV2 create(PersonVOV2 person) {
        logger.info("Create One Person");
        var entity = mapper.convertVoToEntity(person);
        return mapper.convertEntityToVo(repository.save(entity));
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updated One Person");

        Person entity = DozerMapper.parseObject(findById(person.getKey()), Person.class);
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var result = repository.save(entity);
        return DozerMapper.parseObject(result, PersonVO.class);
    }

    public void delete(Long id) {
        logger.info("Delete One Person");
        repository.deleteById(id);
    }
}
