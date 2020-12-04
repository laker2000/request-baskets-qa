package tests.api.responses.put;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestResponsesPutWrongConfiguration extends TestConfig {

    /**
     * Scenario: Check Update response settings with wrong configuration
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a basket with default response settings
     * 3. Try to change response settings providing wrong connfiguration
     * 4. Assert 422 status code
     *
     * */
    @Test
    public void testResponsesPutWrongConfiguration(){
        // 1. Arrange a basket for creation
        String basketName = RandomStringUtils.randomAlphanumeric(10);
        HashMap<String, Object> wrongResponseSettings = new HashMap<>();
        wrongResponseSettings.put("status", 999 );

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);

        // 2. Create a basket with default response settings
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);

        var response = request.post(basketByName);
        assertThat(response.statusCode(), is(201));
        String basketToken = response.body().jsonPath().get("token");

        // 3. Try to change response settings providing wrong connfiguration
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addPathParam("method", "GET");
        builder.addHeader("Authorization", basketToken);

        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        request.body(wrongResponseSettings);
        response = request.put(responses);

        // 4. Assert 422 status code
        assertThat(response.statusCode(), is(422));


    }

}
