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

public class CreateUserPositiveTest {

    private static String userEmail;
    private static String userPassword;
    private static String userName;
    private String accessToken;
    public static boolean keepUser = false;



    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
        Faker faker = new Faker();

        userEmail = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
        userPassword = faker.regexify("[a-zA-Z0-9]{8}");
        userName = faker.name().firstName();

    }

    @Test
    @DisplayName("Test creating a user successfully")
    @Description("Checks success when create user with all required fields")
    public void testCreateUserSuccess() {
        keepUser = true;

        String requestBody = String.format(
                "{\"email\": \"%s\", " +
                        "\"password\": \"%s\"," +
                        "\"name\": \"%s\"}",
                userEmail, userPassword, userName
        );

        System.out.println("Email: " + userEmail);
        System.out.println("Password: " + userPassword);
        System.out.println("Name: " + userName);

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
