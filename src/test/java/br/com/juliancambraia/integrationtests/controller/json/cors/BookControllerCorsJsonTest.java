package br.com.juliancambraia.integrationtests.controller.json.cors;

import br.com.juliancambraia.config.TestConfigs;
import br.com.juliancambraia.data.vo.v1.BookVO;
import br.com.juliancambraia.integrationtests.swagger.testcontainers.AbstractTestIntegration;
import br.com.juliancambraia.integrationtests.vo.security.AccountCredentialVO;
import br.com.juliancambraia.integrationtests.vo.security.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerCorsJsonTest extends AbstractTestIntegration {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookVO bookVO;

    private static Calendar myCal = Calendar.getInstance();

    @BeforeAll
    public static void setUp() throws ParseException {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        bookVO = new BookVO();

        String dt = "2021-7-5";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        myCal.setTime(sdf.parse(dt));
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
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .body(bookVO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO createBook = objectMapper.readValue(content, BookVO.class);
        bookVO = createBook;

        assertNotNull(createBook);
        assertNotNull(createBook.getId());
        assertNotNull(createBook.getAuthor());
        assertNotNull(createBook.getTitle());
        assertNotNull(createBook.getPrice());
        assertNotNull(createBook.getLaunchDate());

        assertTrue(createBook.getId() > 0);

        assertEquals("Roger S. Pressman", createBook.getAuthor());
        assertEquals("Engenharia de Softwaare", createBook.getTitle());
        assertEquals(212.00, createBook.getPrice());
        assertEquals(myCal.getTime(), createBook.getLaunchDate());
    }
    @Test
    @Order(2)
    void testCreateWithWrongOrigin() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .body(bookVO)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
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
        BookVO book = objectMapper.readValue(content, BookVO.class);

        assertNotNull(book);
        assertNotNull(book.getId());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getTitle());
        assertNotNull(book.getPrice());
        assertNotNull(book.getLaunchDate());

        assertTrue(book.getId() > 0);

        assertEquals("Roger S. Pressman", book.getAuthor());
        assertEquals("Engenharia de Softwaare", book.getTitle());
        assertEquals(212.00, book.getPrice());
        assertEquals(myCal.getTime(), book.getLaunchDate());
    }

    @Test
    @Order(3)
    void testFindByIdWithWrongOrigin() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .pathParam("id", bookVO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockBook() {
        bookVO.setAuthor("Roger S. Pressman");
        bookVO.setTitle("Engenharia de Softwaare");
        bookVO.setPrice(212.00);
        bookVO.setLaunchDate(myCal.getTime());
    }
}
