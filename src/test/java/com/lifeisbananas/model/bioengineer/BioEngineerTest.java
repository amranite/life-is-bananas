package com.lifeisbananas.model.bioengineer;

import com.lifeisbananas.model.formula.Formula;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
class BioEngineerTest {

    private BioEngineer bioEngineer;
    private Formula formula1;
    @BeforeEach
    void setup() {
        bioEngineer = new BioEngineer("Ali", "Ali.manera@hotmail.com", "0411223344", "vriesdroog");
        formula1 = new Formula(2, "banaan", "finaal", "vriesdroog", bioEngineer);
    }








    @Test
    void createFormula1() {
        bioEngineer.createFormula(formula1);
        assertEquals(bioEngineer, formula1.getBioEngineer());
        assertTrue((bioEngineer.getFormulas().contains(formula1)));
    }

    @Test
    void createFormula2() {
        formula1.setBioEngineer(null);
        bioEngineer.createFormula(formula1);
        assertEquals(bioEngineer, formula1.getBioEngineer());
        assertTrue((bioEngineer.getFormulas().contains(formula1)));
    }
    @Test
    void createFormula3() {
        formula1 = null;
        assertThrows(NullPointerException.class, () -> bioEngineer.createFormula(formula1));
    }

    @Test
    void developProduct() {
    }

    @Test
    void generateFreezeDryInstruction() {
    }
}