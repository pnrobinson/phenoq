package org.monarchinitiative.phenoq.questionnaire;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenoq.except.PhenoqRuntimeException;
import org.monarchinitiative.phenoq.phenoitem.AgeThresholdPhenoItem;
import org.monarchinitiative.phenoq.phenoitem.SimplePhenoItem;

import java.io.File;
import java.net.URL;
import java.util.List;

public class DevelopmentQuestionnare {

    private final List<SimplePhenoItem> simplePhenoItemList;
    private final List<AgeThresholdPhenoItem> ageThresholdPhenoItemList;


    public DevelopmentQuestionnare(Ontology ontology ) {
        String developmentFilePath = "/questions/development.txt";

        URL url = DevelopmentQuestionnare.class.getResource(developmentFilePath);
        if (url == null) {
            throw new PhenoqRuntimeException("Could not find resource \"" + developmentFilePath + "\"");
        }
        File f = new File(url.getFile());
        if (! f.isFile()) {
            throw new PhenoqRuntimeException("Could not find file " + f.getAbsolutePath());
        }
        QuestionParser parser = new QuestionParser(f, ontology);
        simplePhenoItemList = parser.getSimplePhenoItemList();
        ageThresholdPhenoItemList = parser.getAgeThresholdPhenoItemList();
    }

    public List<SimplePhenoItem> getSimplePhenoItemList() {
        return simplePhenoItemList;
    }

    public List<AgeThresholdPhenoItem> getAgeThresholdPhenoItemList() {
        return ageThresholdPhenoItemList;
    }
}
