package userTests;


import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderWithoutAuthorizationPositiveTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";

    }

    @Test
    @DisplayName("Test creating an order successfully")
    @Description("Checks success when create an order with ingredients")
    public void testCreateOrderWithoutAuthorizationWithIngredientsSuccess() {

        String requestBody = String.format("{\n" +
                "  \"ingredients\": [\n" +
                "    \"61c0c5a71d1f82001bdaaa6d\",\n" +
                "    \"61c0c5a71d1f82001bdaaa6f\"\n" +
                "  ]\n" +
                "}");


        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/orders")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

}
