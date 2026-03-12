package orderWithoutAuthorizationTests;

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

public class CreateOrderWithoutAuthorizationNegativeTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";

    }

    @Test
    @DisplayName("Test creating an order with missing body")
    @Description("Checks error when create an order without ingredients")
    public void testCreateOrderWithoutAuthorizationWithoutIngredientsError() {

        String requestBody = "{}";


        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/orders")
                .then()
                .log().all()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Test creating an order with wrong body")
    @Description("Checks error when create an order with wrong hash of ingredients")
    public void testCreateOrderWithWrongIngredientsHashError() {

        String requestBody = String.format("{\n" +
                "  \"ingredients\": [\n" +
                "    \"61c0c5a71d1f82001bdaaa6d\",\n" +
                "    \"61c0c5a71d1f8200f\"\n" +
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
                .statusCode(500);
    }

}



