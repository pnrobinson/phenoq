package org.monarchinitiative.phenoq.phenoitem;

public class AgeRule {


    private final PhenoAge thresholdAge;
    private final boolean youngerThanAbnormal;

    private AgeRule(PhenoAge thresholdAge, boolean younger){
        this.thresholdAge = thresholdAge;
        this.youngerThanAbnormal = younger;
    }

    AnswerType interpret(PhenoAge age) {
        int c = this.thresholdAge.compareTo(age);
        if (youngerThanAbnormal) {
            if (c<=0) return  AnswerType.OBSERVED;
            else return AnswerType.EXCLUDED;
        } else {
            if (c<=0) return  AnswerType.EXCLUDED;
            else return AnswerType.OBSERVED;
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
        StringBuilder sb = new StringBuilder();
        sb.append(thresholdAge.getYears()).append(" Y").append(thresholdAge.getMonths()).append(" M");
        if (thresholdAge.getDays()!=0) {
            sb.append(thresholdAge.getDays()).append(" D");
        }
        String ageString = sb.toString();
        if (youngerThanAbnormal) {
            return "younger than " + ageString;
        } else {
            return "older than " + ageString;
        }
    }


}
