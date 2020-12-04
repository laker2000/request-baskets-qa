package tests.api.responses.put;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestResponsesPutBadRequest extends TestConfig {

    /**
     * Scenario: Check Bad Request -> Failed to parse JSON into response configuration object.
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a basket with default response settings
     * 3. Try to change response settings with invalid json value for a headers
     * 4. Assert 400 status code
     *
     * */
    @Test
    public void testResponsesPutBadRequest(){
        // 1. Arrange a basket for creation
        String basketName = RandomStringUtils.randomAlphanumeric(10);
        String invalid_json = "{a,b}";

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);


        // 2. Create a basket with default response settings
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);

        var response = request.post(basketByName);
        assertThat(response.statusCode(), is(201));
        String basketToken = response.body().jsonPath().get("token");

        // 3. Try to change response settings with invalid json value for a headers
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addPathParam("method", "GET");
        builder.addHeader("Authorization", basketToken);

        HashMap<String, Object> responseSettings = new HashMap<>();
        responseSettings.put("status", 299 );
        responseSettings.put("headers", invalid_json );
        responseSettings.put("body", "Marko");
        responseSettings.put("is_template", true);

        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        request.body(responseSettings);
        response = request.put(responses);

        // 4. Assert 400 status code
        assertThat(response.statusCode(), is(400));


    }

}
