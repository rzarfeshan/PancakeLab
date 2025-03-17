package org.pancakelab.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.PancakeRecipe;;

public class PancakeDataStore {
	private List<Order> orders;
	private Set<UUID> completedOrders;
	private Set<UUID> preparedOrders;
	private Map<UUID, List<PancakeRecipe>> pancakes;

	public PancakeDataStore() {
		orders = new ArrayList<>();
		completedOrders = new HashSet<>();
		preparedOrders = new HashSet<>();
		pancakes = new ConcurrentHashMap<>();
	}

	public Order getOrder(UUID orderId) {
		return orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
	}

	public void setOrder(Order order) {
		this.orders.add(order);
	}

	public Set<UUID> getCompletedOrders() {
		return completedOrders;
	}

	public synchronized void setCompletedOrders(UUID orderId) {
		this.completedOrders.add(orderId);
	}

	public Set<UUID> getPreparedOrders() {
		return Collections.unmodifiableSet(preparedOrders);
	}

	public synchronized void setPreparedOrders(UUID orderId) {
		this.preparedOrders.add(orderId);
	}

	public List<PancakeRecipe> getPancakes(UUID orderId) {
		return Collections.unmodifiableList(pancakes.getOrDefault(orderId, Collections.emptyList()));
	}

	public void setPancakes(UUID orderId, PancakeRecipe pancakes) {
		this.pancakes.computeIfAbsent(orderId, k -> new ArrayList<>()).add(pancakes);
	}

	public synchronized void cancelOrder(UUID orderId) {
		pancakes.remove(orderId);
		orders.removeIf(o -> o.getId().equals(orderId));
		completedOrders.removeIf(u -> u.equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));

	}

	public synchronized void prepareOrder(UUID orderId) {
		setPreparedOrders(orderId);
		completedOrders.removeIf(u -> u.equals(orderId));
	}

	public List<String> viewOrder(UUID orderId) {
		return getPancakes(orderId).stream().filter(pancake -> pancake.getOrderId().equals(orderId))
				.map(PancakeRecipe::description).collect(Collectors.toUnmodifiableList());

	}

	public synchronized void deliverOrder(UUID orderId) {
		pancakes.remove(orderId);
		orders.removeIf(o -> o.getId().equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));
	}

	public synchronized AtomicInteger removePancake(UUID orderId, String description, int count) {
		final AtomicInteger removedCount = new AtomicInteger(0);

		pancakes.get(orderId).removeIf(pancake -> {
			return pancake.getOrderId().equals(orderId) && pancake.getPancakeDescription().equals(description)
					&& removedCount.getAndIncrement() < count;
		});

		return removedCount;
	}

}
