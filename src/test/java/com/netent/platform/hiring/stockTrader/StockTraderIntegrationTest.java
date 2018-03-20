package com.netent.platform.hiring.stockTrader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.netent.platform.hiring.stockTrader.api.DematServiceIF;
import com.netent.platform.hiring.stockTrader.api.InvalidTransactionRequestException;
import com.netent.platform.hiring.stockTrader.api.Stock;
import com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF;
import com.netent.platform.hiring.stockTrader.api.StockTransferTransactionRequest;
import com.netent.platform.hiring.stockTrader.api.TradingServiceIF;
import com.netent.platform.hiring.stockTrader.impl.StockTraderServiceFactory;

/**
 * The StockTrader integration test. This test must pass before the application
 * is submitted for evaluation.
 *
 * @author aditya.bhushan
 *
 */
public final class StockTraderIntegrationTest {

	private static final Stock NETENT = new Stock("netent");
	private static final Stock GOOGLE = new Stock("googl");
	private static final Stock MICROSOFT = new Stock("msft");
	private static final Stock ADOBE = new Stock("adbe");

	private DematServiceIF dematService;
	private TradingServiceIF tradingService;
	private StockTraderServiceFactoryIF serviceFactory = new StockTraderServiceFactory();

	@Before
	public void setupInitialData() {
		serviceFactory.bootstrap();
		this.dematService = serviceFactory.dematService();
		this.tradingService = serviceFactory.tradingService();
	}

	@Test
	public void testAllocateStocks() throws Exception {
		dematService.allocateStock("user1", NETENT, 10);
		dematService.allocateStock("user1", GOOGLE, 20);

		assertEquals(10, dematService.getStockStatus("user1", NETENT).get().intValue());

		dematService.allocateStock("user1", GOOGLE, 10);
		assertEquals(30, dematService.getStockStatus("user1", GOOGLE).get().intValue());

		try {
			dematService.allocateStock("user1", GOOGLE, -10);
			fail("Negative allocation must be forbidden");
		} catch (InvalidTransactionRequestException e) {
		}
	}

	@Test
	public void testStockTransferTransaction() throws Exception {
		setupInitialStocks();

		StockTransferTransactionRequest t1 = StockTransferTransactionRequest.builder().user("user1").stock(NETENT).quantity(-5)
				.build();
		StockTransferTransactionRequest t2 = StockTransferTransactionRequest.builder().user("user2").stock(NETENT).quantity(5)
				.build();

		StockTransferTransactionRequest t3 = StockTransferTransactionRequest.builder().user("user2").stock(GOOGLE).quantity(-20)
				.build();
		StockTransferTransactionRequest t4 = StockTransferTransactionRequest.builder().user("user1").stock(GOOGLE).quantity(10)
				.build();
		StockTransferTransactionRequest t5 = StockTransferTransactionRequest.builder().user("user3").stock(GOOGLE).quantity(10)
				.build();

		StockTransferTransactionRequest t6 = StockTransferTransactionRequest.builder().user("user2").stock(ADOBE).quantity(-20)
				.build();
		StockTransferTransactionRequest t7 = StockTransferTransactionRequest.builder().user("user3").stock(ADOBE).quantity(20)
				.build();

		tradingService.executeStockTransfer(Arrays.asList(t1, t2, t3, t4, t5, t6, t7));

		assertEquals(5, dematService.getStockStatus("user1", NETENT).get().intValue());
		assertEquals(25, dematService.getStockStatus("user2", NETENT).get().intValue());

		assertEquals(30, dematService.getStockStatus("user1", GOOGLE).get().intValue());
		assertEquals(10, dematService.getStockStatus("user3", GOOGLE).get().intValue());

		assertEquals(10, dematService.getStockStatus("user2", ADOBE).get().intValue());
		assertEquals(20, dematService.getStockStatus("user3", ADOBE).get().intValue());

		// Three transactions for user2. Allocation of shares not counted as
		// transfer transactions.
		assertEquals(3, tradingService.findTransferTransactions("user2").orElse(new ArrayList<>()).size());
	}

	@Test
	public void testStockTransferTransactionImbalanced() throws Exception {
		setupInitialStocks();

		StockTransferTransactionRequest t1 = StockTransferTransactionRequest.builder().user("user1").stock(NETENT).quantity(-5)
				.build();
		StockTransferTransactionRequest t2 = StockTransferTransactionRequest.builder().user("user2").stock(NETENT).quantity(10)
				.build();

		try{
			tradingService.executeStockTransfer(Arrays.asList(t1, t2));
			fail("Must fail on imbalanced transaction request");
		}catch(InvalidTransactionRequestException e){

		}

	}

	private void setupInitialStocks() throws Exception {
		dematService.allocateStock("user1", NETENT, 10);
		dematService.allocateStock("user1", GOOGLE, 20);

		dematService.allocateStock("user2", NETENT, 20);
		dematService.allocateStock("user2", GOOGLE, 30);
		dematService.allocateStock("user2", ADOBE, 30);

		dematService.allocateStock("user3", MICROSOFT, 500);
	}

	@After
	public void cleanup() {
		serviceFactory.cleanup();
	}
}
