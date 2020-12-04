package tests.api.responses.put;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestResponsesPut extends TestConfig {

    /**
     * Scenario: Check Update response settings
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a basket with default response settings
     * 3. Change response settings
     * 4. Assert that given changes are applied
     *
     * */
    @Test
    public void testResponsesPut(){
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


        // 3. Change response settings
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addPathParam("method", "GET");
        builder.addHeader("Authorization", basketToken);

        HashMap<String, Object> responseSettings = new HashMap<>();
        responseSettings.put("status", 299 );
        responseSettings.put("body", "Marko");
        responseSettings.put("is_template", true);

        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        request.body(responseSettings);

        response = request.put(responses);
        assertThat(response.statusCode(), is(204));

        // 4. Assert that given changes are applied
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addPathParam("method", "GET");
        builder.addHeader("Authorization", basketToken);
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);

        response = request.get(responses);
        assertThat(response.statusCode(), is(200));

        // 4. Assert default response schema
        assertThat(response.getBody().jsonPath().get("status"), is(299));
        assertThat(response.getBody().jsonPath().get("body"), is("Marko"));
        assertThat(response.getBody().jsonPath().get("is_template"), is(true));

    }

}
