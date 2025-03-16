package org.pancakelab.model.pancakes;

import java.util.List;

public class Hazelnuts extends PancakeRecipeDecorator {

	public Hazelnuts(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}

	@Override
	public List<String> ingredients() {
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add("hazelnuts");
		return ingredientsList;
	}

}
