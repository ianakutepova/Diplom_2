import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserWithoutRequiredFieldsNegativeTest extends BaseTest {

    private String accessToken;

    @Test
    @DisplayName("Test create user with missing email")
    @Description("Checks error when create user without email")
    public void testCreateUserWithoutEmailError() {
        String[] userData = createUniqueUser();
        User userWithoutEmail = new User(null, userData[1], userData[2]);

        userPostRequest(Endpoints.REGISTER, userWithoutEmail, null)
                .then()
                .statusCode(SC_FORBIDDEN)
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test create user with missing password")
    @Description("Checks error when create user without password")
    public void testCreateUserWithoutPasswordError() {
        String[] userData = createUniqueUser();
        User userWithoutPassword = new User(userData[0], null, userData[2]);

        userPostRequest(Endpoints.REGISTER, userWithoutPassword, null)
                .then()
                .statusCode(SC_FORBIDDEN)
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test create a user with missing name")
    @Description("Checks error when create user without name")
    public void testCreateUserWithoutNameError() {
        String[] userData = createUniqueUser();
        User userWithoutName = new User(userData[0], userData[1], null);

        userPostRequest(Endpoints.REGISTER, userWithoutName, null)
                .then()
                .statusCode(SC_FORBIDDEN)
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
