package com.imdbclone.imdbclone.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix="remote-urls.rapid")
public class RapidApiProperties {
    private String accessKey;
    private String host;
    private String searchMovie;
    private String getRatings;
    private String getPlot;
    private String getOverview;
}
