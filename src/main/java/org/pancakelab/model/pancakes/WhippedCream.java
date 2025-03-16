package org.pancakelab.model.pancakes;

import java.util.List;

public class WhippedCream extends PancakeRecipeDecorator {

	public WhippedCream(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}

	@Override
	public List<String> ingredients() {
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add("whipped cream");
		return ingredientsList;
	}
}
