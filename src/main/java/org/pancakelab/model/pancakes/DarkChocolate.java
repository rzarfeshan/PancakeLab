package org.pancakelab.model.pancakes;

import java.util.List;

import org.pancakelab.util.Ingredients;

public class DarkChocolate extends PancakeRecipeDecorator {

	public DarkChocolate(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}

	@Override
	public List<String> ingredients() {
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add(Ingredients.DARK_CHOCOLATE.getName());
		return ingredientsList;
	}
}
