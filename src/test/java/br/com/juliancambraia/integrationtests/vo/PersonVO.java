package br.com.juliancambraia.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serial;
import java.io.Serializable;
@XmlRootElement
public class PersonVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    public PersonVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PersonVO personVO = (PersonVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, personVO.id)
                .append(firstName, personVO.firstName)
                .append(lastName, personVO.lastName)
                .append(address, personVO.address)
                .append(gender, personVO.gender)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .append(firstName)
                .append(lastName)
                .append(address)
                .append(gender)
                .toHashCode();
    }
}