package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.UUID;

public abstract class PancakeRecipeDecorator implements PancakeRecipe {

	private PancakeRecipe pancakeRecipe;

	public PancakeRecipeDecorator(PancakeRecipe pancakeRecipe) {
		this.pancakeRecipe = pancakeRecipe;
	}

	@Override
	public UUID getOrderId() {
		return pancakeRecipe.getOrderId();
	}

	@Override
	public List<String> ingredients() {
		return pancakeRecipe.ingredients();
	}

	@Override
	public String getPancakeDescription() {
		return pancakeRecipe.getPancakeDescription();
	}

}
