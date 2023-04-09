package br.com.juliancambraia.unittests.mapper.mocks.services;

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
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

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
        entity.setId(1L);

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
        var list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);
        var result = service.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());

        var personOne = result.get(1);

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
        var persisted = entity;
        persisted.setId(1L);
        var vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(vo);

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