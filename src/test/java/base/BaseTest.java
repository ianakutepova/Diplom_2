package base;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.After;
import org.junit.Before;
import static org.apache.http.HttpStatus.*;

public class BaseTest {
    protected RequestSpecification spec;
    private ApiMethods apiMethods;
    public String accessToken;
    public String[] userData;
    public boolean keepUser;

    public void setKeepUser(boolean keepUser) {
        this.keepUser = keepUser;
    }

    @Before
    @Step
    public void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri(Endpoints.BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
        System.out.println("Spec initialized: " + spec);

        apiMethods = new ApiMethods(spec);

        userData = createUniqueUser(); // Вызываем метод для создания уникальных данных пользователя перед каждым тестом
    }

    protected String[] createUniqueUser() {
        Faker faker = new Faker();
        String email = (faker.name().lastName() + faker.regexify("[0-9]{4}") + "@example.com").toLowerCase();
        String password = faker.regexify("[a-zA-Z0-9]{8}");
        String name = faker.name().firstName();

        return new String[]{email, password, name};
    }


    @Step
    protected Response userPostRequest(String endpoint, Object body, String accessToken) {
        return apiMethods.userPostRequest(endpoint, body, accessToken);
    }


    @Step
    protected Response orderPostRequest(String endpoint, Object body, String accessToken) {
        return apiMethods.orderPostRequest(endpoint, body, accessToken);
    }

    @After
    @Step
    public void cleanUpUser() {
        if (!keepUser && accessToken != null) {
            apiMethods.deleteUser(accessToken, Endpoints.DELETE)
                    .then()
                    .statusCode(SC_ACCEPTED);
        }
    }

}