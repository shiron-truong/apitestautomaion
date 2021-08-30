package com.baseclass;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.shiron.api.config.Configuration;
import com.shiron.api.config.ConfigurationManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.FileReader;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.config.ParamConfig.UpdateStrategy.REPLACE;
import static io.restassured.config.ParamConfig.paramConfig;

public class TestBase {
    public static Response response;
    public static RestAssuredConfig configParams;
    protected static Configuration configuration;
    public RequestSpecification defaultRequestSpecification;
    public Logger logger;
    public Object[][] dataMethod;

    @BeforeClass
    public void init() {
        logger = Logger.getLogger(TestBase.class.getName());
        PropertyConfigurator.configure("src/test/resources/log4j.properties");
        logger.setLevel(Level.DEBUG);

        configuration = ConfigurationManager.getConfiguration();

        baseURI = configuration.baseURI();
        basePath = configuration.basePath();
        port = configuration.port();
        configParams = config().paramConfig(paramConfig().queryParamsUpdateStrategy(REPLACE));

        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        defaultRequestSpecification = new RequestSpecBuilder().
                setBaseUri(configuration.baseURI()).
                setBasePath(configuration.basePath()).
                setPort(configuration.port()).setConfig(configParams).
                addQueryParam("appid", configuration.appid()).
                build();
    }

    @BeforeSuite
    public void createDataFromCSV() {
        dataMethod = new Object[][]{};
        String suiteName = this.getClass().getSimpleName();
        String CSV_file = "src/test/resources/testdata/weather/" + suiteName + "Data.csv";
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try {
            FileReader filereader = new FileReader(CSV_file);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();
            // Read all data at once
            List<String[]> allData = csvReader.readAll();
            dataMethod = allData.toArray(new Object[0][]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
