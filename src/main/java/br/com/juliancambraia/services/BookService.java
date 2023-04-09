package br.com.juliancambraia.services;

import br.com.juliancambraia.config.ModelMapperConfig;
import br.com.juliancambraia.controller.BookController;
import br.com.juliancambraia.data.vo.v1.BookVO;
import br.com.juliancambraia.exceptions.RequiredObjectIsNullException;
import br.com.juliancambraia.exceptions.ResourceNotFoundException;
import br.com.juliancambraia.model.Book;
import br.com.juliancambraia.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    Logger logger = Logger.getLogger(BookService.class.getName());
    BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookVO> findAll() {
        logger.info("Findall Books");

        var books = repository.findAll();
        var lists = new ArrayList<BookVO>();
        books.forEach(book -> {
            var vo = ModelMapperConfig.parseObject(book, BookVO.class);
            vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
            lists.add(vo);
        });
        return lists;

    }

    public BookVO findById(Long id) {
        logger.info("Finding Book");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = ModelMapperConfig.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());

        return vo;
    }

    public BookVO create(BookVO book) {
        logger.info("Create one Book");

        if (book == null) throw new RequiredObjectIsNullException();

        var entity = ModelMapperConfig.parseObject(book, Book.class);
        var vo = ModelMapperConfig.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());

        return vo;
    }

    public BookVO update(BookVO book) {
        logger.info("Updated One Book");

        if (book == null) throw new RequiredObjectIsNullException();

        Book entity = ModelMapperConfig.parseObject(findById(book.getId()), Book.class);
        entity.setAuthor(book.getAuthor());
        entity.setTitle(book.getTitle());
        entity.setPrice(book.getPrice());
        entity.setLaunchDate(book.getLaunchDate());

        var result = repository.save(entity);
        var vo = ModelMapperConfig.parseObject(result, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {
        logger.info("Delete One Book");
        repository.deleteById(id);
    }
}
