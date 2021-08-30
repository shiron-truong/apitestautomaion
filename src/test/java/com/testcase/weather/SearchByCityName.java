package com.testcase.weather;

import com.baseclass.TestBase;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;

import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;


public class SearchByCityName extends TestBase {

    @DataProvider(name = "testData")
    public Object[][] prepareDataForMethod(Method method) {
//        if (method.getName().equals("checkLanguageSupport")){
//            System.out.println("PrepareData" + method.getName());
//        }
        Object[][] currentMethodData = new Object[dataMethod.length][];
        Iterator itr = Arrays.stream(dataMethod).iterator();
        int i = 0;
        while (itr.hasNext()) {
            Object[] tmp = ((Object[]) itr.next());
            if (tmp[0].toString().equals(method.getName())) {
                currentMethodData[i] = tmp;
                i++;
            }
        }

        return currentMethodData;
    }


    @Test(priority = 1, dataProvider = "testData")
    public void checkJsonSchema(String methodName, String input, String postMethod,
                                String code, String expected) throws InterruptedException {
        logger.info("Started TC001_checkJsonSchema");
        defaultRequestSpecification.
                queryParam("mode", "json").
                queryParam("q", input);
        given().spec(defaultRequestSpecification).
                when().
                get().
                then().
                assertThat().
                statusCode(Integer.parseInt(code)).
                and().
                contentType(ContentType.JSON).
                body(matchesJsonSchemaInClasspath("schemas/weather/weather_response_schema.json"));

        Thread.sleep(3);
    }

    @Test(priority = 2, dataProvider = "testData")
    public void checkXMLSchema(String methodName, String input, String postMethod,
                               String code, String expected) throws InterruptedException {
        logger.info("Started TC001_checkXMLSchema");
        defaultRequestSpecification.
                queryParam("mode", "xml").
                queryParam("q", input);
        given().spec(defaultRequestSpecification).
                when().
                get().
                then().
                assertThat().
                statusCode(Integer.parseInt(code)).
                and().
                contentType(ContentType.XML).
                body(matchesXsdInClasspath("schemas/weather/weather_response_schema.xsd"));
        Thread.sleep(3);
    }

    @Test(priority = 3, dataProvider = "testData")
    public void checkExactCityName(String methodName, String input, String postMethod,
                                   String statusCode, String expected) {
        System.out.println("checkExactCityName testData[0] :" + methodName);

    }

    @Test(priority = 4, dataProvider = "testData")
    public void checkSimilarCityName(String methodName, String input, String postMethod,
                                     String statusCode, String expected) {
        System.out.println("checkSimilarCityName testData[0] :" + methodName);
    }

    @Test(priority = 5, dataProvider = "testData")
    public void checkLanguageSupport(String methodName, String input, String postMethod,
                                     String code, String expected) throws InterruptedException {
        System.out.println("checkSimilarCityName testData[0] :" + methodName);
        logger.info("Started TC001_checkJsonSchema");
        defaultRequestSpecification.
                queryParam("mode", "json").
                queryParam("q", input);
        given().spec(defaultRequestSpecification).
                when().
                get().
                then().
                assertThat().
                statusCode(Integer.parseInt(code)).
                contentType(ContentType.JSON).
                body(matchesJsonSchemaInClasspath("schemas/weather/weather_response_schema.json")).
                body("name", equalTo(expected));
        Thread.sleep(3);
    }

}

//Positive:
//City name
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