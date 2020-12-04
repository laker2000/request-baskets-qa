package tests.api.service;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import tests.TestConfig;


public class TestService extends TestConfig {

    @Test
    public void testServiceGetVersion()
    {
        when()
            .get(version)
        .then()
            .statusCode(200)
        .and()
            .body("name", equalTo("request-baskets"))
            .body("version", equalTo("n/a"))
            .body("commit", equalTo("n/a"))
            .body("commit_short", equalTo("n/a"))
            .body("source_code", equalTo("https://github.com/darklynx/request-baskets"));

    }

}
