package org.monarchinitiative.phenoq.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    private final String applicationUiTitle;

    private final String version;

    private final String hpoOboUrl;


    @Autowired
    public ApplicationProperties(@Value("${spring.application.ui.title}") String uiTitle,
                                 @Value("${application.version}") String version,
                                 @Value("${hpo.path}") String hpoJson) {
        this.applicationUiTitle = uiTitle;
        this.version = version;
        this.hpoOboUrl = hpoJson;
    }

    public String getApplicationUiTitle() {
        return applicationUiTitle;
    }

    public String getVersion() {
        return version;
    }

    public String getHpoOboUrl() {
        return hpoOboUrl;
    }

}
