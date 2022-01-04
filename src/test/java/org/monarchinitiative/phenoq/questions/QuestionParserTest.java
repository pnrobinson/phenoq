package org.monarchinitiative.phenoq.questions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.phenoq.phenoitem.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionParserTest {

    private static List<SimplePhenoItem> simplePhenoItemList;
    private static List<AgeThresholdPhenoItem> ageThresholdPhenoItemList;

    @BeforeAll
    private static void init() throws FileNotFoundException {
        String hpJsonFilePath = "/obo/hp_small.json";
        URL hpoURL = QuestionParserTest.class.getResource(hpJsonFilePath);
        if (hpoURL == null) {
            throw new FileNotFoundException("Could not find " + hpJsonFilePath);
        }
        Ontology ontology = OntologyLoader.loadOntology(new File(hpoURL.getFile()));
        String questionFilePath = "/questions/example-questions.csv";
        URL url = QuestionParserTest.class.getResource(questionFilePath);
        if (url == null) {
            throw new FileNotFoundException("Could not find " + questionFilePath);
        }
        File f = new File(url.getFile());
        if (! f.isFile()) {
            throw new FileNotFoundException("Could not find file " + f.getAbsolutePath());
        }
        QuestionParser parser = new QuestionParser(f, ontology);
        simplePhenoItemList = parser.getSimplePhenoItemList();
        ageThresholdPhenoItemList = parser.getAgeThresholdPhenoItemList();
    }

    @Test
    public void if_parsed_1_simple_then_OK() {
        assertEquals(1, simplePhenoItemList.size());
    }

    @Test
    public void if_parsed_1_ageThreshold_then_OK() {
        assertEquals(1, ageThresholdPhenoItemList.size());
    }

    @Test
    public void checkSimple() {
        SimplePhenoItem item = simplePhenoItemList.get(0);
        assertNotNull(item);
        Term term =  item.term();
        assertNotNull(term);
        TermId expectedId = TermId.of("HP:0001166");
        assertEquals(expectedId, term.getId());
        assertEquals("Arachnodactyly", item.termLabel());
    }

    @Test
    public void checkAgeThreshold() {
        AgeThresholdPhenoItem item = ageThresholdPhenoItemList.get(0);
        assertNotNull(item);
        Term term = item.term();
        assertNotNull(term);
        TermId expectedId = TermId.of("HP:0031936");
        assertEquals(expectedId, term.getId());
        assertEquals("Delayed ability to walk", item.termLabel());
        Optional<AgeRule> opt = item.ageRuleOpt();
        assertTrue(opt.isPresent());
        AgeRule ageRule = opt.get();
        // Age rule is 1y6m, olderthan
        PhenoAge age17months = new PhenoAge(1,5);
        PhenoAge age18months = new PhenoAge(1,6);
        PhenoAge age19months = new PhenoAge(1,7);
        assertEquals(AnswerType.EXCLUDED, ageRule.interpret(age17months));
        assertEquals(AnswerType.EXCLUDED, ageRule.interpret(age18months));
        assertEquals(AnswerType.OBSERVED, ageRule.interpret(age19months));
    }


}
