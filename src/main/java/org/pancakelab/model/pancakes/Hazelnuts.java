package org.pancakelab.model.pancakes;

import java.util.List;

import org.pancakelab.util.Ingredients;

public class Hazelnuts extends PancakeRecipeDecorator {

	public Hazelnuts(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}

	@Override
	public List<String> ingredients() {
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add(Ingredients.HAZELNUTS.getName());
		return ingredientsList;
	}

}
