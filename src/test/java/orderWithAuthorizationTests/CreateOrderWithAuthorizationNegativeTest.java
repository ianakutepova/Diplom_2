package orderWithAuthorizationTests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderWithAuthorizationNegativeTest {

        private static String userEmail;
        private static String userPassword;
        private static String userName;
        private static String accessToken;


        @BeforeClass
        public static void setUp() {
            RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
            Faker faker = new Faker();

            userEmail = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
            userPassword = faker.regexify("[a-zA-Z0-9]{8}");
            userName = faker.name().firstName();

            String requestBody = String.format(
                    "{\"email\": \"%s\", " +
                            "\"password\": \"%s\"," +
                            "\"name\": \"%s\"}",
                    userEmail, userPassword, userName
            );

            accessToken = given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/auth/register")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("success", equalTo(true))
                    .body("user.email", equalTo(userEmail))
                    .body("user.name", equalTo(userName))
                    .body("accessToken", notNullValue())
                    .body("refreshToken", notNullValue())
                    .extract().path("accessToken");

            System.out.println("Access Token: " + accessToken);
        }

    @Test
    @DisplayName("Test creating an order with missing body")
    @Description("Checks error when create an order without ingredients")
        public void testCreateOrderWithAuthorizationWithoutIngredientsSuccess() {

            String requestBody = "{}";

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
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
    public void testCreateOrderWithAuthorizationWithWrongIngredientsHashError() {

        String requestBody = String.format("{\n" +
                "  \"ingredients\": [\n" +
                "    \"61c0c5a71d1f82001bdaaa6d\",\n" +
                "    \"61c0c5a71d1f8200f\"\n" +
                "  ]\n" +
                "}");


        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestBody)
                .when()
                .post("/api/orders")
                .then()
                .log().all()
                .statusCode(500);
    }

}


