package br.com.juliancambraia.integrationtests.controller.json;

import br.com.juliancambraia.config.TestConfigs;
import br.com.juliancambraia.data.vo.v1.BookVO;
import br.com.juliancambraia.integrationtests.swagger.testcontainers.AbstractTestIntegration;
import br.com.juliancambraia.integrationtests.vo.security.AccountCredentialVO;
import br.com.juliancambraia.integrationtests.vo.security.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractTestIntegration {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookVO bookVO;

    private static Calendar myCal = Calendar.getInstance();
    private static Calendar myCal2 = Calendar.getInstance();

    @BeforeAll
    public static void setUp() throws ParseException {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        bookVO = new BookVO();

        String dt = "2021-7-5";
        String dt2 = "2017-11-29";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        myCal.setTime(sdf.parse(dt));
        myCal2.setTime(sdf.parse(dt2));
    }

    @Test
    @Order(0)
    void authorization() throws JsonProcessingException {
        AccountCredentialVO user = new AccountCredentialVO("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockBook();

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(bookVO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO createdBook = objectMapper.readValue(content, BookVO.class);
        bookVO = createdBook;

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Roger S. Pressman", createdBook.getAuthor());
        assertEquals("Engenharia de Softwaare", createdBook.getTitle());
        assertEquals(212.00, createdBook.getPrice());
        assertEquals(myCal.getTime(), createdBook.getLaunchDate());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        bookVO.setPrice(150.00);

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(bookVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO updatedBook = objectMapper.readValue(content, BookVO.class);
        bookVO = updatedBook;

        assertNotNull(updatedBook);
        assertNotNull(updatedBook.getId());
        assertNotNull(updatedBook.getAuthor());
        assertNotNull(updatedBook.getTitle());
        assertNotNull(updatedBook.getPrice());
        assertNotNull(updatedBook.getLaunchDate());

        assertTrue(updatedBook.getId() > 0);

        assertEquals("Roger S. Pressman", updatedBook.getAuthor());
        assertEquals("Engenharia de Softwaare", updatedBook.getTitle());
        assertEquals(150.00, updatedBook.getPrice());
        assertEquals(myCal.getTime(), updatedBook.getLaunchDate());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        mockBook();
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .pathParam("id", bookVO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO searchOneBook = objectMapper.readValue(content, BookVO.class);
        bookVO = searchOneBook;

        assertNotNull(searchOneBook);
        assertNotNull(searchOneBook.getId());
        assertNotNull(searchOneBook.getAuthor());
        assertNotNull(searchOneBook.getTitle());
        assertNotNull(searchOneBook.getPrice());
        assertNotNull(searchOneBook.getLaunchDate());

        assertTrue(searchOneBook.getId() > 0);

        assertEquals("Roger S. Pressman", searchOneBook.getAuthor());
        assertEquals("Engenharia de Softwaare", searchOneBook.getTitle());
        assertEquals(150.00, searchOneBook.getPrice());
        assertEquals(myCal.getTime(), searchOneBook.getLaunchDate());
    }

    @Test
    @Order(4)
    void testDelete() throws JsonProcessingException {
        given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", bookVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void testFindAll() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<BookVO> book = objectMapper.readValue(content, new TypeReference<>() {
        });
        var foundOneBook = book.get(0);

        assertNotNull(foundOneBook.getId());
        assertNotNull(foundOneBook.getAuthor());
        assertNotNull(foundOneBook.getTitle());
        assertNotNull(foundOneBook.getPrice());
        assertNotNull(foundOneBook.getLaunchDate());

        assertEquals(1, foundOneBook.getId());

        assertEquals("Michael C. Feathers", foundOneBook.getAuthor());
        assertEquals("Working effectively with legacy code", foundOneBook.getTitle());
        assertEquals(49.00, foundOneBook.getPrice());
        assertEquals(myCal2.getTime(), foundOneBook.getLaunchDate());
    }

    private void mockBook() {
        bookVO.setAuthor("Roger S. Pressman");
        bookVO.setTitle("Engenharia de Softwaare");
        bookVO.setPrice(212.00);
        bookVO.setLaunchDate(myCal.getTime());
    }
}
