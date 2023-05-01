package br.com.juliancambraia.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
public class BookVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String author;
    private Date launchDate;

    private Double price;

    private String title;

    public BookVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BookVO bookVO = (BookVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, bookVO.id)
                .append(author, bookVO.author)
                .append(launchDate, bookVO.launchDate)
                .append(price, bookVO.price)
                .append(title, bookVO.title)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .append(author)
                .append(launchDate)
                .append(price)
                .append(title)
                .toHashCode();
    }
}
