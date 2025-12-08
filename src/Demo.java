import com.lifeisbananas.model.bioengineer.BioEngineer;
import com.lifeisbananas.model.enums.UnitType;
import com.lifeisbananas.model.formula.Formula;
import com.lifeisbananas.model.formula.Ingredient;
import com.lifeisbananas.model.nutrition.NutritionalValue;
import com.lifeisbananas.model.rawmaterial.FruitRawMaterial;

public class Demo {
    public static void main (String[] args) {
        // Create a bio-engineer
        BioEngineer bioEngineer = new BioEngineer(
                "Dr. Sigrid Severijns",
                "sigrid@lifeisbananas.com",
                "+3212345678",
                "Fruit Processing"
        );

        bioEngineer.setId(1L);

        // Create nutritional value
        NutritionalValue bananaNutritionalValue = new NutritionalValue(
                89.0, 1.1, 22.8, 0.3, 2.6, 12.2, 0.001,
                3.0, 8.7, 5.0, 0.26
        );

        // Create raw material
        FruitRawMaterial banana = new FruitRawMaterial(
                "FRUIT-001",
                "Banana",
                true,
                2.50,
                "kg"
        );


        banana.setNutritionalValue(bananaNutritionalValue);
        banana.setRipenessLevel("ripe");
        banana.setSugarContent(12.2);
        banana.setAcidityLevel(4.5);

        // Create formula
        Formula formula = new Formula(
                "FORM-001",
                "Banana Chips",
                "1.0",
                "Slice and freeze-dry"
        );
        bioEngineer.createFormula(formula);

        // Create ingredient
        Ingredient ingredient = new Ingredient(500.0, UnitType.GRAM, 1);
        ingredient.setRawMaterial(banana);
        formula.addIngredient(ingredient);

        // Small printing test
        System.out.println("Engineer: " + bioEngineer.getName());
        System.out.println("Formula: " + formula.getName());
        System.out.println("Ingredients: " + formula.getIngredients().size());
        System.out.println("Raw Material: " + banana.getName());
        System.out.println("Calories: " + banana.getNutritionalValue().getCalories());

    }
}
