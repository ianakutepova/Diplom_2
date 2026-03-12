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
import static userTests.CreateUserPositiveTest.keepUser;


public class CreateUserWithoutRequiredFieldsNegativeTest {
    private static String userEmail;
    private static String userPassword;
    private static String userName;
    private String accessToken;


    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
        Faker faker = new Faker();

        userEmail = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
        userPassword = faker.regexify("[a-zA-Z0-9]{8}");
        userName = faker.name().firstName();
    }

    @Test
    @DisplayName("Test create user with missing email")
    @Description("Checks error when create user without email")
    public void testCreateUserWithoutEmailError() {
        // Создаём запрос без email
        String requestBody = String.format("{\"password\": \"%s\", \"name\": \"%s\"}", userPassword, userName);

        System.out.println("Password: " + userPassword);
        System.out.println("Name: " + userName);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403) // Ожидаем ошибку 403
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test create user with missing password")
    @Description("Checks error when create user without password")
    public void testCreateUserWithoutPasswordError() {
        // Создаём запрос без password
        String requestBody = String.format("{\"email\": \"%s\", \"name\": \"%s\"}", userEmail, userName);

        System.out.println("Email: " + userEmail);
        System.out.println("Name: " + userName);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403) // Ожидаем ошибку 403
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test create a user with missing name")
    @Description("Checks error when create user without name")
    public void testCreateUserWithoutNameError() {
        // Создаём запрос без name
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", userEmail, userPassword);

        System.out.println("Email: " + userEmail);
        System.out.println("Password: " + userPassword);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403) // Ожидаем ошибку 403
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void cleanUpUser() {
        if (!keepUser && accessToken != null) {
            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", accessToken)
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .statusCode(202);
        }
    }

}
