package be.lifeisbananas.model.bioengineer;

import be.lifeisbananas.model.formula.Formula;
import be.lifeisbananas.model.instruction.FreezeDryInstruction;
import be.lifeisbananas.model.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
class BioEngineerTest {

    private BioEngineer bioEngineer;
    private Formula formula1;

    @BeforeEach
    void setUp() {
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
    void generateFreezeDryInstruction_nullInstruction_throwsNullPointerException() {
        // Arrange
        FreezeDryInstruction nullInstruction = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> bioEngineer.generateFreezeDryInstruction(nullInstruction));
    }

    @Test
    void generateFreezeDryInstruction_validInstruction_addsInstructionAndSetsBioEngineer() {
        // Arrange
        FreezeDryInstruction instruction = new FreezeDryInstruction(
                1,
                "Banana Chips Formula",
                "1.0",
                "1. Wash bananas\n2. Slice to 3mm\n3. Freeze at -40C\n4. Dry for 24h",
                "Handle with care"
        );

        // Act
        FreezeDryInstruction result = bioEngineer.generateFreezeDryInstruction(instruction);

        // Assert
        assertNotNull(result, "Resultaat mag niet null zijn");
        assertSame(instruction, result, "Geretourneerde instruction moet hetzelfde object zijn");
        assertEquals(bioEngineer, result.getBioEngineer(), "BioEngineer moet ingesteld zijn");
        assertTrue(bioEngineer.getFreezeDryInstructions().contains(instruction), "Instruction moet in lijst zitten");
        assertEquals(1, bioEngineer.getFreezeDryInstructions().size(),"Lijst moet 1 instruction bevatten" );
    }

    @Test
    @DisplayName("EP1: null product gooit NullPointerException")
    void developProduct_nullProduct_throwsNullPointerException() {
        // Arrange
        Product nullProduct = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> bioEngineer.developProduct(nullProduct),
                "Verwachtte een NullPointerException");
    }

    @Test
    @DisplayName("EP2: geldig product wordt correct toegevoegd")
    void developProduct_validProduct_addsProductAndSetsBioEngineer() {
        // Arrange
        Product product = new Product(
                1,
                "Banana Chips",
                "Crispy freeze-dried banana chips",
                365,
                100,
                "BATCH-001"
        );

        // Act
        Product result = bioEngineer.developProduct(product);

        // Assert
        assertNotNull(result, "Resultaat mag niet null zijn");
        assertSame(product, result, "Geretourneerde product moet hetzelfde object zijn");
        assertEquals(bioEngineer, result.getBioEngineer(), "BioEngineer moet ingesteld zijn");
        assertTrue(bioEngineer.getProducts().contains(product), "Product moet in lijst zitten");
        // FOUT 2: Verkeerde verwachte waarde - zou 1 moeten zijn
        assertEquals(1, bioEngineer.getProducts().size(), "Lijst moet 1 product bevatten");
    }

}