package be.lifeisbananas.acceptancetests;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Acceptance Test Suite voor Life is Bananas
 * Voert alle Cucumber feature files uit met Serenity BDD reporting
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/",
        glue = "be.lifeisbananas"
)
public class AcceptanceTestSuite {
}
