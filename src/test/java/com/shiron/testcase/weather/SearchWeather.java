package com.shiron.testcase.weather;

import com.shiron.baseclass.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.*;
import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class SearchWeather extends TestBase {

    /**
     * prepareDataForMethod: collect data for the run test method from data file
     * @param method - object Method
     * @return Data object (Object[][])
     */
    @DataProvider(name = "testData")
    public Object[][] prepareDataForMethod(Method method) {
        Object[][] currentMethodData = new Object[dataMethod.length][];
        Iterator <Object[]> rows = Arrays.stream(dataMethod).iterator();
        int i = 0;
        while (rows.hasNext()) {
            Object[] row = rows.next();
            if (row[0].toString().equals(method.getName())) {
                currentMethodData[i] = row;
                i++;
            }
        }
        return currentMethodData;
    }

    /**
     * parseExpectedData: parse [field=value] string to map<String, String>
     * @param expected - expected string (get from data file)
     * @return mapExpected - Key-value
     */
    private Map<String, String> parseExpectedData(String expected) {
        Map<String, String> mapExpected = new HashMap<>();
        if (expected.contains("=")) {
            String[] keyValue = expected.trim().split("\\s*=\\s*");
            if (keyValue.length == 2) {
                mapExpected.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return mapExpected;
    }

    /**
     * validateExpected: validate value of a key in body json with input
     * @param validatableResponse - ValidatableResponse object from the test method
     * @param expected - Key-value need to be validate
     */
    private void validateExpected(ValidatableResponse validatableResponse,
                                  Map<String, String> expected) {
        String expectedKey = expected.entrySet().iterator().next().getKey();
        String expectedValue = expected.entrySet().iterator().next().getValue();
        validatableResponse.body(expectedKey, containsStringIgnoringCase(expectedValue));
    }

    /**
     * @param dataArgs - Data from dataprovider
     */
    @Test(priority = 1, dataProvider = "testData")
    public void checkJsonSchema(String[] dataArgs){
        lifecycle.updateTestCase(testResult ->
                testResult.setName(dataArgs[dataArgs.length - 1]));
        defaultRequestSpecification.
                queryParam("q", dataArgs[1]);
        given().spec(defaultRequestSpecification).
                when().
                get().
                then().
                assertThat().
                statusCode(Integer.parseInt(dataArgs[2])).
                contentType(ContentType.JSON).
                body(matchesJsonSchemaInClasspath("schemas/weather/weather_response_schema.json"));
    }

    /**
     * @param dataArgs - Data from dataprovider
     */
    @Test(priority = 2, dataProvider = "testData")
    public void checkXMLSchema(String[] dataArgs){
        lifecycle.updateTestCase(testResult -> testResult.setName(dataArgs[dataArgs.length - 1]));
        defaultRequestSpecification.
                queryParam("mode", "xml").
                queryParam("q", dataArgs[1]);
        given().spec(defaultRequestSpecification).
                when().
                get().
                then().
                assertThat().
                statusCode(Integer.parseInt(dataArgs[2])).
                contentType(ContentType.XML).
                body(matchesXsdInClasspath("schemas/weather/weather_response_schema.xsd"));
    }

    /**
     * checkCityName: test method to test all cases from test data file
     * @param dataArgs - Data from dataprovider
     */
    @Test(priority = 3, dataProvider = "testData", dependsOnMethods = "checkJsonSchema")
    public void checkCityName(String[] dataArgs) {
        Map<String, String> expected = parseExpectedData(dataArgs[3]);
        lifecycle.updateTestCase(testResult -> testResult.setName(dataArgs[dataArgs.length - 1]));
        defaultRequestSpecification.
                queryParam("q", dataArgs[1]);
        ValidatableResponse validatableResponse =
                given().spec(defaultRequestSpecification).
                        when().
                        get().
                        then().
                        assertThat().
                        statusCode(Integer.parseInt(dataArgs[2])).
                        contentType(ContentType.JSON);
        if (expected.size() > 0) {
            validateExpected(validatableResponse, expected);
        }
    }

}
