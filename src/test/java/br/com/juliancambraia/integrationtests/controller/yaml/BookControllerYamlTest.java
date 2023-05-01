package br.com.juliancambraia.integrationtests.controller.yaml;

import br.com.juliancambraia.config.TestConfigs;
import br.com.juliancambraia.integrationtests.controller.yaml.mapper.YamlMapper;
import br.com.juliancambraia.integrationtests.swagger.testcontainers.AbstractTestIntegration;
import br.com.juliancambraia.integrationtests.vo.BookVO;
import br.com.juliancambraia.integrationtests.vo.security.AccountCredentialVO;
import br.com.juliancambraia.integrationtests.vo.security.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractTestIntegration {
    private static RequestSpecification specification;
    private static YamlMapper objectMapper;
    private static BookVO bookVO;
    private static Calendar myCal = Calendar.getInstance();
    private static Calendar myCal2 = Calendar.getInstance();

    @BeforeAll
    public static void setUp() throws ParseException {
        objectMapper = new YamlMapper();

        String dt = "2021-7-5";
        String dt2 = "2017-11-29";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        myCal.setTime(sdf.parse(dt));
        myCal2.setTime(sdf.parse(dt2));

        bookVO = new BookVO();
    }

    @Test
    @Order(0)
    void authorization() throws JsonProcessingException {
        AccountCredentialVO user = new AccountCredentialVO("leandro", "admin123");
        var accessToken = given()
                .config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(user, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, objectMapper)
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

        var createdBook = given()
                .config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(bookVO, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, objectMapper);

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Docker Deep Dive", createdBook.getTitle());
        assertEquals("Nigel Poulton", createdBook.getAuthor());
        assertEquals(55.99, createdBook.getPrice());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        bookVO.setId(2L);
        bookVO.setPrice(200.99);

        var updatedBook = given()
                .config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(bookVO, objectMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, objectMapper);

        assertNotNull(updatedBook.getId());
        assertNotNull(updatedBook.getTitle());
        assertNotNull(updatedBook.getAuthor());
        assertNotNull(updatedBook.getPrice());

        assertEquals(updatedBook.getId(), bookVO.getId());
        assertEquals("Docker Deep Dive", updatedBook.getTitle());
        assertEquals("Nigel Poulton", updatedBook.getAuthor());
        assertEquals(200.99, updatedBook.getPrice());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        mockBook();

        var foundBook = given()
                .config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", bookVO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, objectMapper);

        assertNotNull(foundBook.getId());
        assertNotNull(foundBook.getTitle());
        assertNotNull(foundBook.getAuthor());
        assertNotNull(foundBook.getPrice());

        assertEquals(foundBook.getId(), bookVO.getId());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals("Docker Deep Dive", foundBook.getTitle());
        assertEquals(200.99, foundBook.getPrice());
    }

    @Test
    @Order(4)
    void testDelete() throws JsonProcessingException {
        given()
                .spec(specification)
                .config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
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
                .config(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO[].class, objectMapper);

        var book = Arrays.asList(content);

        var foundBookOne = book.get(0);

        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getPrice());
        assertTrue(foundBookOne.getId() > 0);
        assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
        assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
        assertEquals(49.00, foundBookOne.getPrice());

        var foundBookSix = book.get(5);

        assertNotNull(foundBookSix.getId());
        assertNotNull(foundBookSix.getTitle());
        assertNotNull(foundBookSix.getAuthor());
        assertNotNull(foundBookSix.getPrice());

        assertTrue(foundBookSix.getId() > 0);
        assertEquals("Eric Freeman, Elisabeth Freeman, Kathy Sierra, Bert Bates", foundBookSix.getAuthor());
        assertEquals("Head First Design Patterns", foundBookSix.getTitle());
        assertEquals(110.00, foundBookSix.getPrice());
    }

    private void mockBook() {
        bookVO.setAuthor("Nigel Poulton");
        bookVO.setTitle("Docker Deep Dive");
        bookVO.setPrice(55.99);
        bookVO.setLaunchDate(myCal.getTime());
    }
}
