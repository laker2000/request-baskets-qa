package tests.api.baskets.post;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsPostWithoutName extends TestConfig {

    /**
     * Scenario: Check creation of a basket without desired basket name in the path
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Try to create a basket without name path parameter
     * 3. Assert that 404 code is received
     *
     * */
    @Test
    public void testBasketsPostWithoutName(){
        // 1. Arrange a basket for creation
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", serviceToken);

        HashMap<String, Object> basketContent = new HashMap<>();
        basketContent.put("forward_url", "https://nba.com");
        basketContent.put("proxy_response", true);
        basketContent.put("insecure_tls", true);
        basketContent.put("expand_path", true);
        basketContent.put("capacity", 321);

        // 2. Try to create a basket without name path parameter
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);
        var postResponse = request.post();

        // 3. Assert that 404 code is received
        assertThat(postResponse.statusCode(), is(404));


    }

}
