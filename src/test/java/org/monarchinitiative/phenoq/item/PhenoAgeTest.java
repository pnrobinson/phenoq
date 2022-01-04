package org.monarchinitiative.phenoq.item;

import org.junit.jupiter.api.Test;
import org.monarchinitiative.phenoq.phenoitem.PhenoAge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhenoAgeTest {

    @Test
    public void test_zero_days() {
        PhenoAge age1 = new PhenoAge(1,5);
        assertEquals(1, age1.getYears());
        assertEquals(5, age1.getMonths());
        assertEquals(0, age1.getDays());
    }

    @Test
    public void test_months_days() {
        PhenoAge age1 = new PhenoAge(42);
        assertEquals(42, age1.getYears());
        assertEquals(0, age1.getMonths());
        assertEquals(0, age1.getDays());
    }


    /**
     * age 1 is less than age2 so age1.compare(age2) is less than zero
     */
    @Test
    public void test_less_than() {
        PhenoAge age1 = new PhenoAge(42, 10, 3);
        PhenoAge age2 = new PhenoAge(43, 10, 3);
        int c = age1.compareTo(age2);
        assertTrue(c < 0);
    }

    /**
     * age 1 is greater than age2 so age1.compare(age2) is greater than zero
     */
    @Test
    public void test_greater_than() {
        PhenoAge age1 = new PhenoAge(42, 10, 3);
        PhenoAge age2 = new PhenoAge(3, 10, 3);
        int c = age1.compareTo(age2);
        assertTrue(c > 0);
    }

    /**
     * age 1 is equal to than age2 so age1.compare(age2) is  zero
     */
    @Test
    public void test_equal_ages() {
        PhenoAge age1 = new PhenoAge(42, 10, 3);
        PhenoAge age2 = new PhenoAge(42, 10, 3);
        int c = age1.compareTo(age2);
        assertEquals(0, c);
    }


}
