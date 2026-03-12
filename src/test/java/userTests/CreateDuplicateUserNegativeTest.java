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

public class CreateDuplicateUserNegativeTest {
    private static String userEmail;
    private static String userPassword;
    private static String userName;
    private String accessToken;
    private boolean keepUser = false;


    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
        Faker faker = new Faker();

        userEmail = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
        userPassword = faker.regexify("[a-zA-Z0-9]{8}");
        userName = faker.name().firstName();

    }

    public void setKeepUser(boolean value) {
        keepUser = value;
    }

    @Test
    @DisplayName("Test creating a duplicate user")
    @Description("Checks error when create user with same credentials")
    public void testCreateDuplicateUserError() {
        // Используем данные, инициализированные в методе @BeforeClass
        String requestBody = String.format(
                "{\"email\": \"%s\", " +
                        "\"password\": \"%s\"," +
                        "\"name\": \"%s\"}",
                userEmail, userPassword, userName
        );

        System.out.println("Email: " + userEmail);
        System.out.println("Password: " + userPassword);
        System.out.println("Name: " + userName);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200) // Ожидаем успешный ответ при первой регистрации
                .log().all()
                .body("success", equalTo(true));

        // Вторая попытка регистрации с теми же данными
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403) // Ожидаем ошибку 403
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
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
                    .statusCode(202)
                    .body("success", equalTo(true))
                    .body("message", equalTo("User successfully removed"));
        }
    }

}



