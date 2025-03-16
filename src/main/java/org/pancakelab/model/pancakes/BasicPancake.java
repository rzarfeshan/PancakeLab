package org.pancakelab.model.pancakes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicPancake implements PancakeRecipe {
	private UUID orderId;

	@Override
	public UUID getOrderId() {
		return orderId;
	}

	@Override
	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	@Override
	public List<String> ingredients() {
		return new ArrayList<>();
	}

}
