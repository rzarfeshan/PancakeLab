package org.pancakelab.model.pancakes;

import java.util.List;

public class MilkChocolate extends PancakeRecipeDecorator {

	public MilkChocolate(PancakeRecipe pancakeRecipe) {
		super(pancakeRecipe);
	}
	
	@Override
	public List<String> ingredients(){
		List<String> ingredientsList = super.ingredients();
		ingredientsList.add("milk chocolate");
		return ingredientsList;
	}

}

