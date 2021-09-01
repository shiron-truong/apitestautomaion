package com.shiron.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:api.properties"})
public interface Configuration extends Config {

    @Key("api.${env}.base.uri")
    @DefaultValue("https://api.openweathermap.org/data/2.5")
    String baseURI();

    @Key("api.${env}.base.path")
    @DefaultValue("/weather")
    String basePath();

    @Key("api.${env}.base.port")
    @DefaultValue("443")
    int port();

    @Key("api.${env}.appid")
    @DefaultValue("061d0294bb63a91cf5ff3c5b350c9412")
    String appid();

    @Key("${env}")
    @DefaultValue("prod")
    String env();

}
