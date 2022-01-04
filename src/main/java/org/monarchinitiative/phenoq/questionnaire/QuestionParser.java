package org.monarchinitiative.phenoq.questionnaire;

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

    private final static List<String> headerFields = List.of("question.type",
            "hp.id", "hp.label", "threshold.year", "threshold.month",
            "threshold.day", "older.younger", "question.text");
    private final static String header = String.join("\t", headerFields);
    private final static int N_HEADER_FIELDS = headerFields.size();

    public QuestionParser(File questionFile, Ontology ontology) {
        simplePhenoItemList = new ArrayList<>();
        ageThresholdPhenoItemList =  new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
            String line = br.readLine();
            if (! line.equals(header)) {
                throw new PhenolRuntimeException("Malformed questions file - did not find header line");
            }
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String [] fields = line.split("\t");
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
                        if (fields.length != N_HEADER_FIELDS) {
                            throw new PhenolRuntimeException("Malformed age.threshold line with "
                                    + fields.length + " fields. " + line);
                        }
                        // The following fields may be left empty to signify n/a, we assign 0 in this case
                        int y = fields[3].length()>0 ? Integer.parseInt(fields[3]) : 0;
                        int m = fields[4].length()>0 ? Integer.parseInt(fields[4]) : 0;
                        int d = fields[5].length()>0 ? Integer.parseInt(fields[5]) : 0;
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
                        String question = fields[7].trim();
                        AgeThresholdPhenoItem item =
                                new AgeThresholdPhenoItem(term, ageRule, question);
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
