package be.lifeisbananas.acceptancetests.steps;

import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.Product;

import io.cucumber.java.Before;
import io.cucumber.java.nl.*;
import io.cucumber.datatable.DataTable;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Step definitions for bioengineer_develop_product.feature
 * Implementeert EP en BVA tests voor BioEngineer.developProduct() methode
 * Test de business logica in de domain laag (validatie regels)
 *
 * Note: Deze acceptance tests focussen op de business rules (EP/BVA validatie)
 * zonder database connectie. De service laag wordt getest in aparte integration tests.
 */
public class MyStepdefs {

    private BioEngineer bioEngineer;
    private Product product;
    private Exception caughtException;

    @Before
    public void before() {
        System.out.println("Before - Initializing test context for business logic validation");
        this.bioEngineer = null;
        this.product = null;
        this.caughtException = null;
    }

    // Achtergrond step: Gegeven een bio-engineer met naam, email en specialisatie
    @Gegeven("een bio-engineer {string} met email {string} en specialisatie {string}")
    public void eenBioEngineerMetEmailEnSpecialisatie(String naam, String email, String specialisatie) {
        // Domain laag instantiëren voor business logic tests
        this.bioEngineer = new BioEngineer(naam, email, null, specialisatie);
    }

    // EP1: Null product scenario
    @Als("de bio-engineer een null product probeert te ontwikkelen")
    public void deBioEngineerEenNullProductProbeertTeOntwikkelen() {
        try {
            // Test de business logica in de domain laag
            // De developProduct methode bevat validatie (EP/BVA tests)
            this.bioEngineer.developProduct(null);
        } catch (Exception e) {
            this.caughtException = e;
        }
    }

    // Abstract Scenario: Product ontwikkelen met datatable
    @Als("de bio-engineer product {string} probeert te ontwikkelen met:")
    public void deBioEngineerProductProbeertTeOntwikkelenMet(String naam, DataTable dataTable) {
        try {
            // Parse de datatable naar een Map
            Map<String, String> productData = dataTable.asMap(String.class, String.class);

            // Maak product aan met data uit de tabel
            int id = Integer.parseInt(productData.get("id"));
            String productNaam = productData.get("naam");
            String beschrijving = productData.get("beschrijving");
            Integer houdbaarheid = Integer.parseInt(productData.get("houdbaarheid"));
            Integer gewicht = Integer.parseInt(productData.get("gewicht"));
            String batchCode = productData.get("batchCode");

            this.product = new Product(id, productNaam, beschrijving, houdbaarheid, gewicht, batchCode);

            // Test de business logica in de domain laag
            // De developProduct methode bevat BVA validatie voor shelfLife en weight
            this.bioEngineer.developProduct(this.product);

        } catch (Exception e) {
            this.caughtException = e;
        }
    }

    // Dan step voor foutmeldingen (EP1 en BVA ongeldige waarden)
    @Dan("komt er een foutmelding {string}")
    public void komtErEenFoutmelding(String verwachteFoutmelding) {
        assertThat(this.caughtException).isNotNull();
        assertThat(this.caughtException.getMessage()).isEqualTo(verwachteFoutmelding);
    }

    // Dan step voor succesvolle toevoeging (EP2 en BVA geldige waarden)
    @Dan("is het product succesvol toegevoegd")
    public void isHetProductSuccesvolToegevoegd() {
        // Controleer dat er geen exception is gegooid
        assertThat(this.caughtException).isNull();

        // Controleer dat product is toegevoegd aan de lijst
        assertThat(this.bioEngineer.getProducts()).contains(this.product);

        // Controleer dat bio-engineer is gekoppeld aan product
        assertThat(this.product.getBioEngineer()).isEqualTo(this.bioEngineer);
    }
}
