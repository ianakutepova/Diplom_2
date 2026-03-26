import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.apache.http.HttpStatus;
import org.apache.http.HttpStatus.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateDuplicateUserNegativeTest extends BaseTest {

    private String accessToken;
    private boolean keepUser = false;

    @Test
    @DisplayName("Test creating a duplicate user")
    @Description("Checks error when create user with same credentials")
    public void testCreateDuplicateUserError() {

        User user = new User(userData[0], userData[1], userData[2]);

        Response response = userPostRequest(Endpoints.REGISTER, user, null);
        accessToken = response.jsonPath().getString("accessToken");

        keepUser = true;

                userPostRequest(Endpoints.REGISTER, user, null)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN) // Ожидаем ошибку 403
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
}



