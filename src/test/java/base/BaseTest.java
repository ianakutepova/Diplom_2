package base;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.After;
import org.junit.Before;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class BaseTest {
    protected RequestSpecification spec;
    public String accessToken;
    String[] userData;
    public boolean keepUser;

    public void setKeepUser(boolean keepUser) {
        this.keepUser = keepUser;
    }
    @Before
    public void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri(Endpoints.BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
        System.out.println("Spec initialized: " + spec);

        userData = createUniqueUser(); // Вызываем метод для создания уникальных данных пользователя перед каждым тестом
    }

    protected String[] createUniqueUser() {
        Faker faker = new Faker();
        String email = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
        String password = faker.regexify("[a-zA-Z0-9]{8}");
        String name = faker.name().firstName();

        return new String[]{email, password, name};
    }

    protected Response userPostRequest(String endpoint, Object body, String accessToken) {
        if (accessToken != null) {
            return given()
                    .spec(spec)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(body)
                    .when()
                    .post(endpoint);
        } else {
            return given()
                    .spec(spec)
                    .body(body)
                    .when()
                    .post(endpoint);
        }
    }

    protected Response orderPostRequest(String endpoint, Object body, String accessToken) {
        if (accessToken != null) {
            return given()
                    .spec(spec)
                    .header("Authorization", accessToken)
                    .body(body)
                    .when()
                    .post(endpoint);
        } else {
            return given()
                    .spec(spec)
                    .body(body)
                    .when()
                    .post(endpoint);
        }
    }


    @After
    public void cleanUpUser() {

        if (!keepUser && accessToken != null) {
            deleteUser(accessToken, Endpoints.DELETE)
                    .then()
                    .statusCode(SC_ACCEPTED);
        }
    }

    private Response deleteUser(String accessToken, String endpoint) {
        return given()
                .spec(spec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete(endpoint);
    }
}