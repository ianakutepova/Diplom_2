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
import static userTests.LoginUserPositiveTest.accessToken;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class IncorrectLoginUserNegativeTest {
    private final String emailPattern;
    private final String passwordPattern;

    public IncorrectLoginUserNegativeTest(String emailPattern, String passwordPattern) {
        this.emailPattern = emailPattern;
        this.passwordPattern = passwordPattern;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"wrong%s", "%s"},
                {"%s", "wrong%s"},
                {null, "%s"},
                {"%s", null}
        });
    }

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
    }

    @Test
    @DisplayName("Test login user with wrong email or password")
    @Description("Checks error when login user with wrong credentials")
    public void testLoginWithIncorrectCredentials() {
        Faker faker = new Faker();
        String userEmail = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
        String userPassword = faker.regexify("[a-zA-Z0-9]{8}");

        String emailToUse;
        String passwordToUse;

        if (emailPattern != null) {
            emailToUse = emailPattern.replace("%s", userEmail);
        } else {
            emailToUse = null;
        }

        if (passwordPattern != null) {
            passwordToUse = passwordPattern.replace("%s", userPassword);
        } else {
            passwordToUse = null;
        }


        String requestBody = String.format(
                "{\"email\": \"%s\", " +
                        "\"password\": \"%s\"}",
                emailToUse,
                passwordToUse
        );

        given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(requestBody)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401) // Ожидаем ответ с ошибкой
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
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