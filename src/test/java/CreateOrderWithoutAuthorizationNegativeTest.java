import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Order;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderWithoutAuthorizationNegativeTest extends BaseTest {


    @Test
    @DisplayName("Test creating an order with missing body")
    @Description("Checks error when create an order without ingredients")
    public void testCreateOrderWithoutAuthorizationWithoutIngredientsError() {
        String[] userData = createUniqueUser();
        String email = userData[0];
        String password = userData[1];
        String name = userData[2];

        Order order = new Order(null);

        Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, null);

        orderResponse.then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Test creating an order with wrong body")
    @Description("Checks error when create an order with wrong hash of ingredients")
    public void testCreateOrderWithWrongIngredientsHashError() {

        List<String> wrongIngredients = Arrays.asList(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f8200f"
        );

        Order order = new Order(wrongIngredients);

        Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, null);

        orderResponse.then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}



