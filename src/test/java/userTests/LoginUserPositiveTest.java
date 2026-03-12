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
import static userTests.CreateUserPositiveTest.keepUser;

public class LoginUserPositiveTest {

    private static String userEmail;
    private static String userPassword;
    private static String userName;
    static String accessToken;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
        Faker faker = new Faker();

        userEmail = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
        userPassword = faker.regexify("[a-zA-Z0-9]{8}");
        userName = faker.name().firstName();

        accessToken = createUser(userEmail, userPassword, userName);

        System.out.println("Generated userEmail: " + userEmail);
        System.out.println("Generated userPassword: " + userPassword);
    }

    private static String createUser(String email, String password, String name) {
        String accessToken = given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"email\": \"%s\", \"password\": \"%s\", \"name\": \"%s\"}", email, password, name))
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .extract().path("accessToken");

        return accessToken;
    }

    @Test
    @DisplayName("Test login user successfully")
    @Description("Checks success when login user with correct credentials")
    public void testLoginSuccess() {
        // Используем корректные данные для входа
        String requestBody = String.format(
                "{\"email\": \"%s\", " +
                        "\"password\": \"%s\"}",
                userEmail, userPassword
        );

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestBody)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200) // Ожидаем успешный ответ
                .log().all()
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(userEmail))
                .body("user.name", equalTo(userName));
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
