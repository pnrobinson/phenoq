package org.monarchinitiative.phenoq.questions;

import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.phenoq.phenoitem.AgeRule;
import org.monarchinitiative.phenoq.phenoitem.AgeThresholdPhenoItem;
import org.monarchinitiative.phenoq.phenoitem.PhenoAge;
import org.monarchinitiative.phenoq.phenoitem.SimplePhenoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionParser.class);

    private final List<SimplePhenoItem> simplePhenoItemList;
    private final List<AgeThresholdPhenoItem> ageThresholdPhenoItemList;

    public QuestionParser(File questionFile, Ontology ontology) {
        simplePhenoItemList = new ArrayList<>();
        ageThresholdPhenoItemList =  new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
            String line = br.readLine();
            if (! line.startsWith("question.type")) {
                throw new PhenolRuntimeException("Malformed questions file - did not find header line");
            }
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String [] fields = line.split(",");
                String label = fields[1].trim();
                TermId tid = TermId.of(fields[2].trim());
                if (! ontology.containsTerm(tid)) {
                    LOGGER.error("Could not find term for {}", line);
                    continue;
                }
                Term term = ontology.getTermMap().get(tid);
                switch (fields[0].toLowerCase(Locale.ROOT)) {
                    case "simple" -> {
                        if (fields.length != 3) {
                            throw new PhenolRuntimeException("Malformed simple line with "
                                    + fields.length + " fields. " + line);
                        }
                        SimplePhenoItem item = new SimplePhenoItem(term);
                        simplePhenoItemList.add(item);
                    }
                    case "age.threshold" -> {
                        if (fields.length != 7) {
                            throw new PhenolRuntimeException("Malformed age.threshold line with "
                                    + fields.length + " fields. " + line);
                        }
                        //age.threshold,Delayed ability to walk,HP:0031936,1,6,,older
                        int y,m,d;
                        if (fields[3].length()>0) {
                            y = Integer.parseInt(fields[3]);
                        } else {
                            y = 0;
                        }
                        if (fields[4].length()>0) {
                            m = Integer.parseInt(fields[4]);
                        } else {
                            m = 0;
                        }
                        if (fields[5].length()>0) {
                            d = Integer.parseInt(fields[5]);
                        } else {
                            d = 0;
                        }
                        PhenoAge phenoAge = new PhenoAge(y,m,d);
                        String rule = fields[6].trim();
                        AgeRule ageRule;
                        if (rule.equals("older")) {
                            ageRule = AgeRule.olderThanIsAbnormal(phenoAge);
                        } else if (rule.equals("younger")) {
                            ageRule = AgeRule.youngerThanIsAbnormal(phenoAge);
                        } else {
                            throw new PhenolRuntimeException("Could not recognize age.rule " + line);
                        }
                        AgeThresholdPhenoItem item =
                                new AgeThresholdPhenoItem(term, ageRule, "Age when first able to walk?");
                        ageThresholdPhenoItemList.add(item);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Could not read question file: {}", e.getMessage());
        }
    }

    public List<SimplePhenoItem> getSimplePhenoItemList() {
        return simplePhenoItemList;
    }

    public List<AgeThresholdPhenoItem> getAgeThresholdPhenoItemList() {
        return ageThresholdPhenoItemList;
    }
}
