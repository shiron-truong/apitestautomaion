package com.shiron.api.specs;

import com.shiron.api.config.Configuration;
import com.shiron.api.config.ConfigurationManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class InitialStateSpecs {

    private InitialStateSpecs() {

    }

    public static RequestSpecification set() {
        Configuration configuration = ConfigurationManager.getConfiguration();
        return new RequestSpecBuilder().
                setBaseUri(configuration.baseURI()).
                setBasePath(configuration.basePath()).
                setPort(configuration.port()).
                build();
    }

}
