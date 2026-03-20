package base;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class ApiMethods {
    protected RequestSpecification spec;

    public ApiMethods(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response userPostRequest(String endpoint, Object body, String accessToken) {
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

    public Response orderPostRequest(String endpoint, Object body, String accessToken) {
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

    public Response deleteUser(String accessToken, String endpoint) {
        return given()
                .spec(spec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete(endpoint);
    }
}
