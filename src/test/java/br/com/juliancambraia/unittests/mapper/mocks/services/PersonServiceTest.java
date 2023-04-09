package br.com.juliancambraia.unittests.mapper.mocks.services;

import br.com.juliancambraia.config.ModelMapperConfig;
import br.com.juliancambraia.data.vo.v1.PersonVO;
import br.com.juliancambraia.exceptions.RequiredObjectIsNullException;
import br.com.juliancambraia.model.Person;
import br.com.juliancambraia.repository.PersonRepository;
import br.com.juliancambraia.services.PersonService;
import br.com.juliancambraia.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

    @Mock
    ModelMapperConfig mapper;

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Person entity = input.mockEntity();
        PersonVO vo = input.mockVO();
        entity.setId(1L);
        when(mapper.parseObject(entity, PersonVO.class)).thenReturn(vo);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test0", result.getAddress());
        assertEquals("First Name Test0", result.getFirstName());
        assertEquals("Last Name Test0", result.getLastName());
        assertEquals("Male", result.getGender());
    }

    @Test
    void findAll() {
        Person entity = input.mockEntity();
        var list = input.mockEntityList();
        var vo = input.mockVO();

        when(repository.findAll()).thenReturn(list);
        when(this.mapper.parseObject(entity, PersonVO.class)).thenReturn(vo);
        var result = service.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());

        var personOne = result.get(1);

        mapper.parseObject(entity, PersonVO.class);

        assertNotNull(personOne.getId());
        assertNotNull(personOne.getLinks());
        assertTrue(result.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());
    }

    @Test
    void create() {
        var entity = input.mockEntity(1);
        var vo2 = mock(PersonVO.class);
        var persisted = entity;
        persisted.setId(1L);
        var vo = input.mockVO(1);
        vo.setId(1L);

        when(mapper.parseObject(vo2, Person.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(persisted);

        mapper.parseObject(vo2, Person.class);
        var result = service.create(vo);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        System.out.println(result.toString());
        assertTrue(result.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void createWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        var entity = input.mockEntity(1);
        var persisted = entity;
        persisted.setId(1L);
        var vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.findById(vo.getId())).thenReturn(Optional.of(entity));
        when(repository.save(persisted)).thenReturn(entity);

        var result = service.update(vo);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void updateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}