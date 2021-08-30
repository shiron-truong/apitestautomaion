package com.shiron.api.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:api.properties"})
public interface Configuration extends Config {

    @Key("api.prod.base.path")
    String basePath();

    @Key("api.prod.base.uri")
    String baseURI();

    @Key("api.prod.base.port")
    int port();

    @Key("api.prod.appid")
    String appid();

}
