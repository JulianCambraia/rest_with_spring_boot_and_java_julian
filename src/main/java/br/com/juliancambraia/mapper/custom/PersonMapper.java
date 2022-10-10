package br.com.juliancambraia.mapper.custom;

import br.com.juliancambraia.data.vo.v2.PersonVOV2;
import br.com.juliancambraia.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {
    public PersonVOV2 convertEntityToVo(Person person) {
        PersonVOV2 vo = new PersonVOV2();
        vo.setFirstName(person.getFirstName());
        vo.setLastName(person.getLastName());
        vo.setAddress(person.getAddress());
        vo.setGender(person.getGender());
        vo.setBirthday(new Date());

        return vo;
    }

    public Person convertVoToEntity(PersonVOV2 vo) {
        Person entity = new Person();
        entity.setFirstName(vo.getFirstName());
        entity.setLastName(vo.getLastName());
        entity.setAddress(vo.getAddress());
        entity.setGender(vo.getGender());

        return entity;
    }
}
