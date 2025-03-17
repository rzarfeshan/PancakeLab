package org.pancakelab.service;

import org.pancakelab.datastore.PancakeDataStore;
import org.pancakelab.exception.ValidationException;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.util.Ingredients;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {

	private PancakeDataStore pancakeDataStore;
	private PancakeFactory pancakeFactory;

	public PancakeService(PancakeDataStore pancakeDataStore) {
		this.pancakeDataStore = pancakeDataStore;
		this.pancakeFactory = new PancakeFactory();
	}

	public Order createOrder(int building, int room) {
		Order order = new Order(building, room);
		pancakeDataStore.setOrder(order);
		return order;
	}

	public void preparePancake(UUID orderId, int count, List<Ingredients> ingredientsListToPrepare) {
		validateOrderId(orderId);
		String dishName = Ingredients.convertIngredientsToString(ingredientsListToPrepare);
		for (int i = 0; i < count; ++i) {
			addPancake(pancakeFactory.preparePancake(ingredientsListToPrepare, dishName, orderId),
					pancakeDataStore.getOrder(orderId));
		}
	}

	private void addPancake(PancakeRecipe pancake, Order order) {
		pancakeDataStore.setPancakes(order.getId(), pancake);

		OrderLog.logAddPancake(order, pancake.description(), pancakeDataStore.getPancakes(order.getId()));
	}

	public void removePancakes(List<Ingredients> ingredientsListToRemove, UUID orderId, int count) {

		if (count <= 0) {
			return; // No pancakes to remove if count is zero or negative
		}

		String description = Ingredients.convertIngredientsToString(ingredientsListToRemove);
		final AtomicInteger removedCount = pancakeDataStore.removePancake(orderId, description, count);

		Order order = pancakeDataStore.getOrder(orderId);
		OrderLog.logRemovePancakes(order, description, removedCount.get(), pancakeDataStore.getPancakes(orderId));
	}

	public void cancelOrder(UUID orderId) {
		Order order = pancakeDataStore.getOrder(orderId);
		OrderLog.logCancelOrder(order, pancakeDataStore.getPancakes(orderId));

		pancakeDataStore.cancelOrder(orderId);

		OrderLog.logCancelOrder(order, pancakeDataStore.getPancakes(orderId));
	}

	public void completeOrder(UUID orderId) {
		pancakeDataStore.setCompletedOrders(orderId);
	}

	public Set<UUID> listCompletedOrders() {
		return pancakeDataStore.getCompletedOrders();
	}

	public void prepareOrder(UUID orderId) {
		pancakeDataStore.prepareOrder(orderId);
	}

	public Set<UUID> listPreparedOrders() {
		return pancakeDataStore.getPreparedOrders();
	}

	public Object[] deliverOrder(UUID orderId) {
		if (!pancakeDataStore.getPreparedOrders().contains(orderId))
			return null;

		Order order = pancakeDataStore.getOrder(orderId);
		List<String> pancakesToDeliver = pancakeDataStore.viewOrder(orderId);
		OrderLog.logDeliverOrder(order, pancakeDataStore.getPancakes(orderId));

		pancakeDataStore.deliverOrder(orderId);

		return new Object[] { order, pancakesToDeliver };
	}

	private void validateOrderId(UUID orderId) {
		if (pancakeDataStore.getOrder(orderId) == null) {
			throw new ValidationException("OrderId doesn not exist. Please create an order to proceed.");
		}

	}

	public List<String> viewOrder(UUID orderId) {
		return pancakeDataStore.viewOrder(orderId);
	}
}
