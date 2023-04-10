package br.com.juliancambraia.integrationtests.swagger;

import br.com.juliancambraia.config.TestConfigs;
import br.com.juliancambraia.integrationtests.swagger.testcontainers.AbstractTestIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractTestIntegration {

    @Test
    void souldDisplaySwaggerUiPage() {
        var content = given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfigs.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("Swagger UI"));
    }

    @Test
    void person_resource_returns_200_with_expected_gender_and_person() {

        given()
                .basePath("/api/person/v1/")
                .port(TestConfigs.SERVER_PORT)
                .when()
                .get("{id}", 5)
                .then()
                .statusCode(200)
                .body("gender", equalTo("Male"));

    }

    @Test
    void person_resource_returns_200_with_expected_address_and_person() {

        given()
                .basePath("/api/person/v1/")
                .port(TestConfigs.SERVER_PORT)
                .when()
                .get("{id}", 1)
                .then()
                .statusCode(200)
                .body("address", equalToIgnoringCase("são Paulo"));

    }
}
