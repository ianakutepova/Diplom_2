import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserPositiveTest extends BaseTest {

    private static String email;
    private static String password;
    private static String name;
    public static String accessToken;
    public static boolean keepUser = false;

    @Test
    @DisplayName("Test creating a user successfully")
    @Description("Checks success when create user with all required fields")
    public void testCreateUserSuccess() {
        keepUser = false;

        String[] userData = createUniqueUser();
        email = userData[0];
        password = userData[1];
        name = userData[2];

        User user = new User(email, password, name);

        accessToken = userPostRequest(Endpoints.REGISTER, user, null)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract().path("accessToken");
    }
}
