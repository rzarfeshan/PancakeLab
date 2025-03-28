package org.pancakelab.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.pancakelab.datastore.PancakeDataStore;
import org.pancakelab.exception.ValidationException;
import org.pancakelab.model.Order;
import org.pancakelab.util.Ingredients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PancakeServiceTest {
	private PancakeDataStore pancakeDataStore = new PancakeDataStore();
	private PancakeService pancakeService = new PancakeService(pancakeDataStore);
	private Order order = null;

	private final static String DARK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious pancake with dark chocolate!";
	private final static String MILK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate!";
	private final static String MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate, hazelnuts!";
	private final static String DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE_DESCRIPTION ="Delicious pancake with dark chocolate, whipped cream!";

	@Test
	@org.junit.jupiter.api.Order(05)
	public void GivenOrderDoesNotExist_Test() {

		assertThrows(ValidationException.class,
				() -> pancakeService.preparePancake(UUID.randomUUID(), 1, List.of(Ingredients.DARK_CHOCOLATE)));

	}

	@Test
	@org.junit.jupiter.api.Order(10)
	public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
		// setup

		// exercise
		order = pancakeService.createOrder(10, 20);

		assertEquals(10, order.getBuilding());
		assertEquals(20, order.getRoom());

		// verify

		// tear down
	}

	@Test
	@org.junit.jupiter.api.Order(20)
	public void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
		// setup

		// exercise
		addPancakes();

		// verify
		List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

		assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION, DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
				DARK_CHOCOLATE_PANCAKE_DESCRIPTION, MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
				MILK_CHOCOLATE_PANCAKE_DESCRIPTION, MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
				MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION, MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
				MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION, DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE_DESCRIPTION), ordersPancakes);

		// tear down
	}

	@Test
	@org.junit.jupiter.api.Order(30)
	public void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
		// setup

		// exercise
		pancakeService.removePancakes(List.of(Ingredients.DARK_CHOCOLATE), order.getId(), 2);
		pancakeService.removePancakes(List.of(Ingredients.MILK_CHOCOLATE), order.getId(), 3);
		pancakeService.removePancakes(List.of(Ingredients.MILK_CHOCOLATE, Ingredients.HAZELNUTS), order.getId(), 1);
		pancakeService.removePancakes(List.of(Ingredients.DARK_CHOCOLATE, Ingredients.WHIPPED_CREAM), order.getId(), 1);

		// verify
		List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

		assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION, MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
				MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

		// tear down
	}

	@Test
	@org.junit.jupiter.api.Order(40)
	public void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
		// setup

		// exercise
		pancakeService.completeOrder(order.getId());

		// verify
		Set<UUID> completedOrdersOrders = pancakeService.listCompletedOrders();
		assertTrue(completedOrdersOrders.contains(order.getId()));

		// tear down
	}

	@Test
	@org.junit.jupiter.api.Order(50)
	public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
		// setup

		// exercise
		pancakeService.prepareOrder(order.getId());

		// verify
		Set<UUID> completedOrders = pancakeService.listCompletedOrders();
		assertFalse(completedOrders.contains(order.getId()));

		Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
		assertTrue(preparedOrders.contains(order.getId()));

		// tear down
	}

	@Test
	@org.junit.jupiter.api.Order(60)
	public void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
		// setup
		List<String> pancakesToDeliver = pancakeService.viewOrder(order.getId());

		// exercise
		Object[] deliveredOrder = pancakeService.deliverOrder(order.getId());

		// verify
		Set<UUID> completedOrders = pancakeService.listCompletedOrders();
		assertFalse(completedOrders.contains(order.getId()));

		Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
		assertFalse(preparedOrders.contains(order.getId()));

		List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

		assertEquals(List.of(), ordersPancakes);
		assertEquals(order.getId(), ((Order) deliveredOrder[0]).getId());
		assertEquals(pancakesToDeliver, (List<String>) deliveredOrder[1]);

		// tear down
		order = null;
	}

	@Test
	@org.junit.jupiter.api.Order(70)
	public void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
		// setup
		order = pancakeService.createOrder(10, 20);
		addPancakes();

		// exercise
		pancakeService.cancelOrder(order.getId());

		// verify
		Set<UUID> completedOrders = pancakeService.listCompletedOrders();
		assertFalse(completedOrders.contains(order.getId()));

		Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
		assertFalse(preparedOrders.contains(order.getId()));

		List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

		assertEquals(List.of(), ordersPancakes);

		// tear down
	}

	private void addPancakes() {
		pancakeService.preparePancake(order.getId(), 3, List.of(Ingredients.DARK_CHOCOLATE));
		pancakeService.preparePancake(order.getId(), 3, List.of(Ingredients.MILK_CHOCOLATE));
		pancakeService.preparePancake(order.getId(), 3, List.of(Ingredients.MILK_CHOCOLATE, Ingredients.HAZELNUTS));
		pancakeService.preparePancake(order.getId(), 1, List.of(Ingredients.DARK_CHOCOLATE, Ingredients.WHIPPED_CREAM));
	}
}
