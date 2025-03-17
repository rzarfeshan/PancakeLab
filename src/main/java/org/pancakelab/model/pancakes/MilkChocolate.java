package org.pancakelab.model.pancakes;

import java.util.List;

import org.pancakelab.util.Ingredients;

public class MilkChocolate extends PancakeRecipeDecorator {

	public MilkChocolate(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}
	
	@Override
	public List<String> ingredients(){
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add(Ingredients.MILK_CHOCOLATE.getName());
		return ingredientsList;
	}

}

