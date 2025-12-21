package be.lifeisbananas;

import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.Product;
import be.lifeisbananas.domain.ProductStatus;
import be.lifeisbananas.service.BioEngineerService;
import be.lifeisbananas.service.FormulaService;
import be.lifeisbananas.service.ProductService;
import be.lifeisbananas.service.impl.BioEngineerServiceImpl;
import be.lifeisbananas.service.impl.FormulaServiceImpl;
import be.lifeisbananas.service.impl.ProductServiceImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class MainController {

    private ProductService productService;
    private BioEngineerService bioEngineerService;
    private FormulaService formulaService;

    private BioEngineer defaultBioEngineer;
    private Formula defaultFormula;

    @FXML
    private TextArea textAreaProductLijst, textAreaMelding, textAreaStatusMelding;

    @FXML
    private TextField textProductNaam, textBeschrijving, textHoudbaarheid,
                      textGewicht, textBatchNummer;

    @FXML
    private TextField textProductNaamStatus, textHuidigeStatus;

    @FXML
    private Button buttonMaakProduct;

    @FXML
    public void initialize() throws SQLException {
        this.productService = new ProductServiceImpl();
        this.bioEngineerService = new BioEngineerServiceImpl();
        this.formulaService = new FormulaServiceImpl();

        // Create default bio-engineer and formula for testing (if they don't exist)
        try {
            // Check if default bio-engineer already exists
            boolean bioEngineerExists = false;
            for (BioEngineer be : bioEngineerService.findAll()) {
                if ("sarah@lifeisbananas.com".equals(be.getEmail())) {
                    this.defaultBioEngineer = be;
                    bioEngineerExists = true;
                    break;
                }
            }

            // Create bio-engineer only if it doesn't exist
            if (!bioEngineerExists) {
                this.defaultBioEngineer = new BioEngineer();
                this.defaultBioEngineer.setName("Dr. Sarah Johnson");
                this.defaultBioEngineer.setEmail("sarah.johnson@lifeisbananas.com");
                this.defaultBioEngineer.setSpecialization("Fruit Processing");
                this.defaultBioEngineer.setPhoneNumber("+32-123-456-789");
                this.bioEngineerService.save(defaultBioEngineer);
            }

            // Check if default formula already exists
            boolean formulaExists = false;
            for (Formula f : formulaService.findAll()) {
                if ("Banana Chips Formula".equals(f.getName()) &&
                    f.getBioEngineer() != null &&
                    f.getBioEngineer().getId() == defaultBioEngineer.getId()) {
                    this.defaultFormula = f;
                    formulaExists = true;
                    break;
                }
            }

            // Create formula only if it doesn't exist
            if (!formulaExists) {
                this.defaultFormula = new Formula();
                this.defaultFormula.setName("Banana Chips Formula");
                this.defaultFormula.setVersion("1.0");
                this.defaultFormula.setCreationDate(java.time.LocalDate.now());
                this.defaultFormula.setLastModified(java.time.LocalDate.now());
                this.defaultFormula.setBioEngineer(defaultBioEngineer);
                this.formulaService.save(defaultFormula);
            }

            textAreaMelding.setText("Systeem geïnitialiseerd.\nStandaard bio-engineer: " +
                                   defaultBioEngineer.getName() + "\nStandaard formule: " +
                                   defaultFormula.getName());
        } catch (Exception e) {
            textAreaMelding.setText("Initialisatie fout: " + e.getMessage());
        }
    }

    private Product leesProductGegevens() {
        Product product = new Product();

        String naam = textProductNaam.getText();
        String beschrijving = textBeschrijving.getText();
        String houdbaarheid = textHoudbaarheid.getText();
        String gewicht = textGewicht.getText();
        String batchNummer = textBatchNummer.getText();

        try {
            product.setName(naam);
            product.setDescription(beschrijving);
            product.setShelfLife(Integer.parseInt(houdbaarheid));
            product.setWeight(Integer.parseInt(gewicht));
            product.setBatchNumber(batchNummer);
            product.setStatus(ProductStatus.IN_DEVELOPMENT);
            product.setFormula(defaultFormula);
            product.setBioEngineer(defaultBioEngineer);
        } catch (NumberFormatException e) {
            textAreaMelding.setText("Fout: Houdbaarheid en Gewicht moeten gehele getallen zijn");
            return null;
        }

        return product;
    }

    private void toonProducten() {
        try {
            String productLijst = "PRODUCTEN OVERZICHT:\n";
            productLijst += "===================\n\n";

            for (Product p : productService.findAll()) {
                productLijst += "Naam: " + p.getName() + "\n";
                productLijst += "Beschrijving: " + p.getDescription() + "\n";
                productLijst += "Status: " + p.getStatus() + "\n";
                productLijst += "Houdbaarheid: " + p.getShelfLife() + " dagen\n";
                productLijst += "Gewicht: " + p.getWeight() + " gram\n";
                productLijst += "Batch: " + p.getBatchNumber() + "\n";
                productLijst += "-------------------\n";
            }

            textAreaProductLijst.setText(productLijst);
        } catch (SQLException e) {
            textAreaMelding.setText("Fout bij ophalen producten: " + e.getMessage());
        }
    }

    @FXML
    void actionMaakProduct(ActionEvent event) {
        Product product = leesProductGegevens();

        if (product != null) {
            try {
                productService.save(product);
                textAreaMelding.setText("Product succesvol aangemaakt!\n" +
                                       "Naam: " + product.getName() + "\n" +
                                       "Status: " + product.getStatus() + "\n" +
                                       "Formule: " + product.getFormula().getName());

                // Clear input fields
                textProductNaam.clear();
                textBeschrijving.clear();
                textHoudbaarheid.clear();
                textGewicht.clear();
                textBatchNummer.clear();

                // Refresh product list
                toonProducten();

            } catch (SQLException e) {
                textAreaMelding.setText("Fout bij opslaan product: " + e.getMessage());
            }
        }
    }

    private Product zoekProductOpNaam(String naam) throws SQLException {
        for (Product p : productService.findAll()) {
            if (p.getName().equalsIgnoreCase(naam)) {
                return p;
            }
        }
        return null;
    }

    @FXML
    void actionMarkeerAlsGetest(ActionEvent event) {
        String productNaam = textProductNaamStatus.getText();

        if (productNaam == null || productNaam.trim().isEmpty()) {
            textAreaStatusMelding.setText("Voer een productnaam in");
            return;
        }

        try {
            Product product = zoekProductOpNaam(productNaam);

            if (product == null) {
                textAreaStatusMelding.setText("Product niet gevonden: " + productNaam);
                return;
            }

            if (product.getStatus() != ProductStatus.IN_DEVELOPMENT) {
                textAreaStatusMelding.setText("Fout: Product moet status IN_DEVELOPMENT hebben.\n" +
                                             "Huidige status: " + product.getStatus());
                return;
            }

            productService.markAsTested(product);

            textHuidigeStatus.setText(product.getStatus().toString());
            textAreaStatusMelding.setText("Product succesvol gemarkeerd als TESTED!\n" +
                                         "Product: " + product.getName());

            toonProducten();

        } catch (SQLException e) {
            textAreaStatusMelding.setText("Fout: " + e.getMessage());
        }
    }

    @FXML
    void actionGoedkeurenProduct(ActionEvent event) {
        String productNaam = textProductNaamStatus.getText();

        if (productNaam == null || productNaam.trim().isEmpty()) {
            textAreaStatusMelding.setText("Voer een productnaam in");
            return;
        }

        try {
            Product product = zoekProductOpNaam(productNaam);

            if (product == null) {
                textAreaStatusMelding.setText("Product niet gevonden: " + productNaam);
                return;
            }

            if (product.getStatus() != ProductStatus.TESTED) {
                textAreaStatusMelding.setText("Fout: Product moet status TESTED hebben.\n" +
                                             "Huidige status: " + product.getStatus() + "\n" +
                                             "Kan niet direct goedkeuren zonder test fase!");
                return;
            }

            productService.approve(product);

            textHuidigeStatus.setText(product.getStatus().toString());
            textAreaStatusMelding.setText("Product succesvol goedgekeurd (APPROVED)!\n" +
                                         "Product: " + product.getName() + "\n" +
                                         "Klaar voor productie");

            toonProducten();

        } catch (SQLException e) {
            textAreaStatusMelding.setText("Fout: " + e.getMessage());
        }
    }

    @FXML
    void actionStartProductie(ActionEvent event) {
        String productNaam = textProductNaamStatus.getText();

        if (productNaam == null || productNaam.trim().isEmpty()) {
            textAreaStatusMelding.setText("Voer een productnaam in");
            return;
        }

        try {
            Product product = zoekProductOpNaam(productNaam);

            if (product == null) {
                textAreaStatusMelding.setText("Product niet gevonden: " + productNaam);
                return;
            }

            if (product.getStatus() != ProductStatus.APPROVED) {
                textAreaStatusMelding.setText("Fout: Product moet status APPROVED hebben.\n" +
                                             "Huidige status: " + product.getStatus() + "\n" +
                                             "Kan niet in productie zonder goedkeuring!");
                return;
            }

            productService.startProduction(product);

            textHuidigeStatus.setText(product.getStatus().toString());
            textAreaStatusMelding.setText("Productie gestart!\n" +
                                         "Product: " + product.getName() + "\n" +
                                         "Status: IN_PRODUCTION");

            toonProducten();

        } catch (SQLException e) {
            textAreaStatusMelding.setText("Fout: " + e.getMessage());
        }
    }
}
