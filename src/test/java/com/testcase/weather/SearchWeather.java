package com.testcase.weather;

import com.baseclass.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.*;
import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class SearchWeather extends TestBase {

    @DataProvider(name = "testData")
    public Object[][] prepareDataForMethod(Method method) {
        Object[][] currentMethodData = new Object[dataMethod.length][];
        Iterator rows = Arrays.stream(dataMethod).iterator();
        int i = 0;
        while (rows.hasNext()) {
            Object[] row = ((Object[]) rows.next());
            if (row[0].toString().equals(method.getName())) {
                currentMethodData[i] = row;
                i++;
            }
        }
        return currentMethodData;
    }

    private Map<String, String> parseExpectedData(String expected){
        Map<String, String> mapExpected = new HashMap<>();
        if (expected.contains("=")){
            String[] keyValue = expected.trim().split("\\s*=\\s*");
            if (keyValue.length == 2){
                mapExpected.put(keyValue[0].trim(),keyValue[1].trim());
            }
        }
        return mapExpected;
    }

    private void validateExpected(ValidatableResponse validatableResponse,
                                  Map<String, String> expected){
        String expectedKey = expected.entrySet().iterator().next().getKey();
        String expectedValue = expected.entrySet().iterator().next().getValue();
        validatableResponse.body(expectedKey, containsStringIgnoringCase(expectedValue));
    }

    @Test(priority = 1, dataProvider = "testData")
    public void checkJsonSchema(String[] dataArgs) throws InterruptedException {
        logger.info("Started :" + dataArgs[0]);
        lifecycle.updateTestCase(testResult -> testResult.setName(dataArgs[dataArgs.length-1]));
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

        Thread.sleep(3);
    }

    @Test(priority = 2, dataProvider = "testData")
    public void checkXMLSchema(String[] dataArgs) throws InterruptedException {
        logger.info("Started: " + dataArgs[0]);
        lifecycle.updateTestCase(testResult -> testResult.setName(dataArgs[dataArgs.length-1]));
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

        Thread.sleep(3);
    }

    @Test(priority = 3, dataProvider = "testData")
    public void checkCityName(String[] dataArgs) throws InterruptedException{
        logger.info("Started: " + dataArgs[0]);
        Map<String, String> expected = parseExpectedData(dataArgs[3]);
        lifecycle.updateTestCase(testResult -> testResult.setName(dataArgs[dataArgs.length-1]));
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
        if (expected.size()>0) {
            validateExpected(validatableResponse, expected);
        }

        Thread.sleep(3);
    }

    @Test(priority = 5, dataProvider = "testData")
    public void checkMultipleLanguageSupport(String[] dataArgs) throws InterruptedException {
        logger.info("Started :" + dataArgs[0]);
        lifecycle.updateTestCase(testResult -> testResult.setName(dataArgs[dataArgs.length-1]));
        Map<String, String> expected = parseExpectedData(dataArgs[3]);
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
            //body("name", equalTo(dataArgs[3]));

        if (expected.size()>0) {
            validateExpected(validatableResponse, expected);
        }
        Thread.sleep(3);
    }

}

//Positive:
//City name x
//City name, state code (US only)
//City name, country code
//City name, state code, country code (US only)
//input city name in multi languages -> return name and description the same language with input
//appid
//negative:
//no data input for q -> 400
//input special charactor
//input long word
//check method: POST,UPDATE,PUT,DELETE
// invalid appid
//http/https

//Data
//testcase | input | expected
//checkWithCityName
//checkWithCityName,StateCode
//checkWithCityName,CountryCode
//checkWithCityname,StateCode,CountryCode
//checkWithCityName_Vi
//checkWithCityName_EN
//checkWithCityName_fr
//checkInvalidAppid
//checkEmptyInput
//CheckSpecialCharactor
//checkLongStringInput
//checkWithHttp

//valid responseField Value

//dataprovider before method