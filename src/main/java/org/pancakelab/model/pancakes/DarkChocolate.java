package org.pancakelab.model.pancakes;

import java.util.List;

public class DarkChocolate extends PancakeRecipeDecorator {

	public DarkChocolate(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}

	@Override
	public List<String> ingredients() {
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add("dark chocolate");
		return ingredientsList;
	}
}
