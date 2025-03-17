package org.pancakelab.model;

import java.util.Objects;
import java.util.UUID;

public class Order {
	private final UUID id;
	private final int building;
	private final int room;
	//Map<String, Integer> orderItems;

	public Order(int building, int room) {
		this.id = UUID.randomUUID();
		this.building = building;
		this.room = room;
		//orderItems = new HashMap<String, Integer>();
	}

	public UUID getId() {
		return id;
	}

	public int getBuilding() {
		return building;
	}

	public int getRoom() {
		return room;
	}

//	public void setOrderItems(String orderItem, Integer numberToIncrementOrDecrement) {
//		orderItems.put(orderItem, orderItems.getOrDefault(orderItem, numberToIncrementOrDecrement));
//	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
