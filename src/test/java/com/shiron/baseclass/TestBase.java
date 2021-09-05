package com.shiron.baseclass;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.shiron.config.Configuration;
import com.shiron.config.ConfigurationManager;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static io.restassured.RestAssured.*;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.ParamConfig.UpdateStrategy.REPLACE;
import static io.restassured.config.ParamConfig.paramConfig;

public class TestBase {
    public static RestAssuredConfig configParams;
    public static Configuration configuration;
    public RequestSpecification defaultRequestSpecification;
    public Logger logger;
    public Object[][] dataMethod;
    public AllureLifecycle lifecycle = Allure.getLifecycle();

    @BeforeSuite(alwaysRun = true)
    public void initBaseClass() {
        logger = Logger.getLogger(TestBase.class.getName());
        PropertyConfigurator.configure("src/test/resources/log4j.properties");
        logger.setLevel(Level.DEBUG);
        configuration = ConfigurationManager.getConfiguration();
        baseURI = configuration.baseURI();
        basePath = configuration.basePath();
        port = configuration.port();
        configParams = config().
                paramConfig(paramConfig().queryParamsUpdateStrategy(REPLACE)).
                encoderConfig(encoderConfig().defaultQueryParameterCharset(StandardCharsets.UTF_8));

        defaultRequestSpecification = new RequestSpecBuilder().
                setBaseUri(configuration.baseURI()).
                setBasePath(configuration.basePath()).
                setPort(configuration.port()).setConfig(configParams).
                addQueryParam("appid", configuration.appid()).
                build();

        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeSuite(dependsOnMethods = "initBaseClass", alwaysRun = true)
    public void createDataFromCSV() {
        dataMethod = new Object[][]{};
        String suiteName = this.getClass().getSimpleName();
        String CSV_file = "src/test/resources/testdata/weather/" +
                configuration.env() + "/" + suiteName + "Data.csv";
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try {
            FileReader filereader = new FileReader(CSV_file, StandardCharsets.UTF_8);
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

    @BeforeMethod(alwaysRun = true)
    public void resetRequestSpecification() {
        defaultRequestSpecification.queryParam("mode", "json");
    }
}
