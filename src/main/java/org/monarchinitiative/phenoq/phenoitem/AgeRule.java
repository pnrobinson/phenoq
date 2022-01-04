package org.monarchinitiative.phenoq.phenoitem;

public class AgeRule {


    private final PhenoAge thresholdAge;
    private final boolean youngerThanAbnormal;

    private AgeRule(PhenoAge thresholdAge, boolean younger){
        this.thresholdAge = thresholdAge;
        this.youngerThanAbnormal = younger;
    }

    public AnswerType interpret(PhenoAge age) {
        int c = this.thresholdAge.compareTo(age);
        if (youngerThanAbnormal) {
                if (c<=0) return  AnswerType.EXCLUDED;
            else return AnswerType.OBSERVED;
        } else {
            if (c<=0) return  AnswerType.OBSERVED;
            else return AnswerType.EXCLUDED;
        }
    }


    public static AgeRule youngerThanIsAbnormal(PhenoAge age) {
        return new AgeRule(age, true);
    }

    public static AgeRule olderThanIsAbnormal(PhenoAge age) {
        return new AgeRule(age, false);
    }

    @Override
    public String toString() {
        if (youngerThanAbnormal) {
            return "younger than " + thresholdAge;
        } else {
            return "older than " + thresholdAge;
        }
    }


}
