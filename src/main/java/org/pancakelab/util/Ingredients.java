package org.pancakelab.util;

import java.util.List;
import java.util.stream.Collectors;

public enum Ingredients {

	DARK_CHOCOLATE("dark chocolate"), MILK_CHOCOLATE("milk chocolate"), HAZELNUTS("hazelnuts"),
	WHIPPED_CREAM("whipped cream");

	private final String name;

	Ingredients(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static String convertIngredientsToString(List<Ingredients> ingredients) {
		return ingredients.stream().map(Ingredients::getName).collect(Collectors.joining(", "));
	}

}
