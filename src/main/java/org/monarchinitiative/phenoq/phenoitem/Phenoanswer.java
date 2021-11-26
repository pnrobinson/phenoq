package org.monarchinitiative.phenoq.phenoitem;

import org.monarchinitiative.phenol.ontology.data.Term;

import java.util.Optional;

public interface Phenoanswer {

    Term term();
    AnswerType answer();
    Optional<PhenoAge> ageOptional();
    boolean observed();
    boolean excluded();
    boolean unknown();

}
