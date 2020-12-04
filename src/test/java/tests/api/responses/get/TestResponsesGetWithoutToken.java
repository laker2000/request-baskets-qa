package tests.api.responses.get;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestResponsesGetWithoutToken extends TestConfig {

    /**
     * Scenario: Check GET response settings without basket token
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a basket with default response settings
     * 3. Try to get response configuration without basket token
     * 4. Assert 401 status code
     *
     * */
    @Test
    public void testResponsesGetWithoutToken(){
        // 1. Arrange a basket for creation
        String basketName = RandomStringUtils.randomAlphanumeric(10);

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);

        // 2. Create a basket with default response settings
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        var response = request.post(basketByName);
        assertThat(response.statusCode(), is(201));
        String basketToken = response.body().jsonPath().get("token");

        // 3.Try to get response configuration with a wrong basket token
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addPathParam("method", "GET");

        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        response = request.get(responses);

        // 4. Assert 401 status code
        assertThat(response.statusCode(), is(401));

    }

}
