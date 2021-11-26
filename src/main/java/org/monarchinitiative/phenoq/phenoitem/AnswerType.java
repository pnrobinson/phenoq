package org.monarchinitiative.phenoq.phenoitem;

public enum AnswerType {

    OBSERVED, EXCLUDED, UNKNOWN;

    @Override
    public String toString() {
        return switch (this) {
            case UNKNOWN -> "U";
            case OBSERVED -> "O";
            case EXCLUDED -> "E";
        };
    }
}
