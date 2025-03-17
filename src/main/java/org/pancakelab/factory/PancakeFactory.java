package org.pancakelab.factory;

import java.util.List;
import java.util.UUID;

import org.pancakelab.model.pancakes.BasicPancake;
import org.pancakelab.model.pancakes.DarkChocolate;
import org.pancakelab.model.pancakes.Hazelnuts;
import org.pancakelab.model.pancakes.MilkChocolate;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.WhippedCream;
import org.pancakelab.util.Ingredients;

public class PancakeFactory {
	PancakeRecipe pancakeRecipe;

	public PancakeRecipe preparePancake(List<Ingredients> ingredientsList, String dishName, UUID orderId) {
		pancakeRecipe = new BasicPancake(dishName, orderId);
		for (Ingredients ingredient : ingredientsList)
			if (ingredient == Ingredients.DARK_CHOCOLATE) {
				pancakeRecipe = new DarkChocolate(pancakeRecipe);
			} else if (ingredient == Ingredients.MILK_CHOCOLATE) {
				pancakeRecipe = new MilkChocolate(pancakeRecipe);
			} else if (ingredient == Ingredients.HAZELNUTS) {
				pancakeRecipe = new Hazelnuts(pancakeRecipe);
			} else if (ingredient == Ingredients.WHIPPED_CREAM) {
				pancakeRecipe = new WhippedCream(pancakeRecipe);
			}
		return pancakeRecipe;

	}

}
