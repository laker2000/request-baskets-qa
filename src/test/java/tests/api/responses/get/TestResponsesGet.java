package tests.api.responses.get;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestResponsesGet extends TestConfig {

    /**
     * Scenario: Check GET response settings
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. 2. Create a basket with default response settings
     * 3. Get responses settings
     * 4. Assert default response schema
     *
     * */
    @Test
    public void testResponsesGet(){
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

        // 3. Get responses settings
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addPathParam("method", "GET");
        builder.addHeader("Authorization", basketToken);
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);

        response = request.get(responses);
        assertThat(response.statusCode(), is(200));

        // 4. Assert default response schema
        assertThat(response.getBody().jsonPath().get("status"), is(200));
        assertThat(response.getBody().jsonPath().get("is_template"), is(false));

    }

}
