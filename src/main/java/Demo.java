import be.lifeisbananas.domain.BioEngineer;
import be.lifeisbananas.domain.UnitType;
import be.lifeisbananas.domain.Formula;
import be.lifeisbananas.domain.Ingredient;
import be.lifeisbananas.domain.NutritionalValue;
import be.lifeisbananas.domain.FruitRawMaterial;

public class Demo {
    public static void main (String[] args) {
        // Create a bio-engineer
        BioEngineer bioEngineer = new BioEngineer(
                "Dr. Sigrid Severijns",
                "sigrid@lifeisbananas.com",
                "+3212345678",
                "Fruit Processing"
        );

        bioEngineer.setId(67);

        // Create nutritional value
        NutritionalValue bananaNutritionalValue = new NutritionalValue(
                89.0, 1.1, 22.8, 0.3, 2.6, 12.2, 0.001,
                3.0, 8.7, 5.0, 0.26
        );

        // Create raw material
        FruitRawMaterial banana = new FruitRawMaterial(
                202,
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
                101,
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
        System.out.println("Engineer ID: " + bioEngineer.getId());
        System.out.println("Formula: " + formula.getName());
        System.out.println("Formula ID: " + formula.getFormulaID());
        System.out.println("Ingredients: " + formula.getIngredients().size());
        System.out.println("Raw Material: " + banana.getName());
        System.out.println("Calories: " + banana.getNutritionalValue().getCalories());

    }
}
