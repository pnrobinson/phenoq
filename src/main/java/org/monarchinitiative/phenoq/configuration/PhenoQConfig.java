package org.monarchinitiative.phenoq.configuration;

import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;


@Configuration
public class PhenoQConfig {

    @Autowired
    ApplicationProperties applicationProperties;

    @Bean
    public Ontology ontology() {
        String path = applicationProperties.getHpoOboUrl();
        return OntologyLoader.loadOntology(new File(path));
    }

}
