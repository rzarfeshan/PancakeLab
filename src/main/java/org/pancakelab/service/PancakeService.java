package org.pancakelab.service;

import org.pancakelab.exception.ValidationException;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.util.Ingredients;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PancakeService {
	private List<Order> orders;
	private Set<UUID> completedOrders;
	private Set<UUID> preparedOrders;
	private List<PancakeRecipe> pancakes;
	private PancakeFactory pancakeFactory;

	public PancakeService() {
		orders = new ArrayList<>();
		completedOrders = new HashSet<>();
		preparedOrders = new HashSet<>();
		pancakes = new ArrayList<>();
		pancakeFactory = new PancakeFactory();
	}

	public Order createOrder(int building, int room) {
		Order order = new Order(building, room);
		orders.add(order);
		return order;
	}

	public void preparePancake(UUID orderId, int count, List<Ingredients> ingredientsList) {
		validateOrderId(orderId);
		for (int i = 0; i < count; ++i) {
			addPancake(pancakeFactory.preparePancake(ingredientsList),
					orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
		}
	}

	@Deprecated
	public void addDarkChocolatePancake(UUID orderId, int count) {
		validateOrderId(orderId);
		for (int i = 0; i < count; ++i) {
			PancakeRecipe p = new BasicPancake();
			addPancake(new DarkChocolate(p), orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());

		}
	}

	@Deprecated
	public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
		validateOrderId(orderId);
		for (int i = 0; i < count; ++i) {
			addPancake(new WhippedCream(new DarkChocolate(new BasicPancake())),
					orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
		}

	}

	@Deprecated
	public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
		validateOrderId(orderId);
		for (int i = 0; i < count; ++i) {
			addPancake(new Hazelnuts(new WhippedCream(new DarkChocolate(new BasicPancake()))),
					orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
		}

	}

	@Deprecated
	public void addMilkChocolatePancake(UUID orderId, int count) {
		validateOrderId(orderId);
		for (int i = 0; i < count; ++i) {
			addPancake(new MilkChocolate(new BasicPancake()),
					orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
		}

	}

	@Deprecated
	public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
		validateOrderId(orderId);
		for (int i = 0; i < count; ++i) {
			addPancake(new Hazelnuts(new MilkChocolate(new BasicPancake())),
					orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());

		}
	}

	public List<String> viewOrder(UUID orderId) {
		return pancakes.stream().filter(pancake -> pancake.getOrderId().equals(orderId)).map(PancakeRecipe::description)
				.collect(Collectors.toList());
	}

	private void addPancake(PancakeRecipe pancake, Order order) {
		pancake.setOrderId(order.getId());
		pancakes.add(pancake);

		OrderLog.logAddPancake(order, pancake.description(), pancakes);
	}

	public void removePancakes(String description, UUID orderId, int count) {

		final AtomicInteger removedCount = new AtomicInteger(0);
		pancakes.removeIf(pancake -> {
			return pancake.getOrderId().equals(orderId) && pancake.description().equals(description)
					&& removedCount.getAndIncrement() < count;
		});

		Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get();
		OrderLog.logRemovePancakes(order, description, removedCount.get(), pancakes);
	}

	public void cancelOrder(UUID orderId) {
		Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get();
		OrderLog.logCancelOrder(order, this.pancakes);

		pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
		orders.removeIf(o -> o.getId().equals(orderId));
		completedOrders.removeIf(u -> u.equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));

		OrderLog.logCancelOrder(order, pancakes);
	}

	public void completeOrder(UUID orderId) {
		completedOrders.add(orderId);
	}

	public Set<UUID> listCompletedOrders() {
		return completedOrders;
	}

	public void prepareOrder(UUID orderId) {
		preparedOrders.add(orderId);
		completedOrders.removeIf(u -> u.equals(orderId));
	}

	public Set<UUID> listPreparedOrders() {
		return preparedOrders;
	}

	public Object[] deliverOrder(UUID orderId) {
		if (!preparedOrders.contains(orderId))
			return null;

		Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get();
		List<String> pancakesToDeliver = viewOrder(orderId);
		OrderLog.logDeliverOrder(order, this.pancakes);

		pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
		orders.removeIf(o -> o.getId().equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));

		return new Object[] { order, pancakesToDeliver };
	}

	private void validateOrderId(UUID orderId) {
		orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow(
				() -> new ValidationException("OrderId doesn not exist. Please create an order to proceed."));

	}
}
