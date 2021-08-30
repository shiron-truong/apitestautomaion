package com.shiron.api.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:api.properties"})
public interface Configuration extends Config {

    @Key("api.${env}.base.path")
    String basePath();

    @Key("api.${env}.base.uri")
    String baseURI();

    @Key("api.${env}.base.port")
    int port();

    @Key("api.${env}.appid")
    String appid();

}
