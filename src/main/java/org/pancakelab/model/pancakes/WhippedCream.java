package org.pancakelab.model.pancakes;

import java.util.List;

import org.pancakelab.util.Ingredients;

public class WhippedCream extends PancakeRecipeDecorator {

	public WhippedCream(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}

	@Override
	public List<String> ingredients() {
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add(Ingredients.WHIPPED_CREAM.getName());
		return ingredientsList;
	}
}
