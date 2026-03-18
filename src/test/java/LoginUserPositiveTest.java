import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserPositiveTest extends BaseTest {

    static String accessToken;

    @Test
    @DisplayName("Test login user successfully")
    @Description("Checks success when login user with correct credentials")
    public void testLoginSuccess() {
        String[] userData = createUniqueUser();
        User user = new User(userData[0], userData[1], userData[2]);

        Response registerResponse = userPostRequest(Endpoints.REGISTER, user, null);
        accessToken = registerResponse.jsonPath().getString("accessToken");

        Response loginResponse = userPostRequest(Endpoints.LOGIN, user, accessToken);

        setKeepUser(true);

        loginResponse.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
}
