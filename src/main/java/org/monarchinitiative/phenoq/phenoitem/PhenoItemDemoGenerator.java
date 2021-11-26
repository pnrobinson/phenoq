package org.monarchinitiative.phenoq.phenoitem;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.*;

public class PhenoItemDemoGenerator {

    private final Ontology hpo;

    public PhenoItemDemoGenerator(Ontology ontology) {
        this.hpo = ontology;
    }

    public List<PhenoItem> generateItems() {
        List<PhenoItem> items = new ArrayList<>();
        Term arachnodactyly = getTerm("HP:0001166");
        items.add(new SimplePhenoItem(arachnodactyly));
        Term triangularFace = getTerm("HP:0000325");
        items.add(new SimplePhenoItem(triangularFace));
        Term delayedAbilityToWalk = getTerm("HP:0031936");
        PhenoAge eighteenMonths = new PhenoAge(1,6);
        AgeRule by18months = AgeRule.olderThanIsAbnormal(eighteenMonths);
        items.add(new AgeThresholdPhenoItem(delayedAbilityToWalk, by18months, "Age when begun to walk?"));
        return items;
    }

    private Term getTerm(String id) {
        TermId tid = TermId.of(id);
        return hpo.getTermMap().get(tid);
    }



}
