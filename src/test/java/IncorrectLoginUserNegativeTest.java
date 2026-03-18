import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class IncorrectLoginUserNegativeTest extends BaseTest {
    static String accessToken;
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


    @Test
    @DisplayName("Test login user with wrong email or password")
    @Description("Checks error when login user with wrong credentials")
    public void testLoginWithIncorrectCredentials() {
        String[] userData = createUniqueUser();
        String email = emailPattern != null ? emailPattern.replace("%s", userData[0]) : null;
        String password = passwordPattern != null ? passwordPattern.replace("%s", userData[1]) : null;

        User user = new User(email, password);
        Response registerResponse = userPostRequest(Endpoints.REGISTER, user, null);
        accessToken = registerResponse.jsonPath().getString("accessToken");

        Response loginResponse = userPostRequest(Endpoints.LOGIN, user, accessToken);

        setKeepUser(true);
        loginResponse.then()
                .statusCode(SC_UNAUTHORIZED) // используем строковую константу из HttpStatus
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

}