package org.pancakelab.model.pancakes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicPancake implements PancakeRecipe {
	private UUID orderId;
	private String dishName;

	public BasicPancake(String dishName, UUID orderId) {
		this.orderId = orderId;
		this.dishName = dishName;
	}

	@Override
	public UUID getOrderId() {
		return orderId;
	}

	@Override
	public List<String> ingredients() {
		return new ArrayList<>();
	}

	@Override
	public String getPancakeDescription() {
		return dishName;
	}

}
