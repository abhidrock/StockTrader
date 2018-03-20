package com.netent.platform.hiring.stockTrader.api;

/**
 * Factory class to get hold of service instances. Will be used by clients to invoke the service.
 * If the service runs remote (in an application server for e.g.), the factory must return remote service clients
 * which can be directly invoked.
 * 
 * Implementation of this class will be used to evaluate the test. An implementation by the name StockTraderServiceFactory
 * must be kept in the "com.netent.platform.hiring.stockTrader.impl" package
 * 
 * @author aditya.bhushan
 *
 */
public interface StockTraderServiceFactoryIF {

	/**
	 * Method to be called by clients before any operations can be executed on StockTrader application.
	 * If a service in the application is invoked before calling bootstrap(), an exception may be thrown.
	 */
	void bootstrap();
	
	/**
	 * Return a handle to the TradingService
	 * @return
	 */
	TradingServiceIF tradingService();
	
	/**
	 * Return a handle to the DematService
	 * @return
	 */
	DematServiceIF dematService();

	/**
	 * Method, when called, will clear up all stored data in the application, and prepare the application for first use. 
	 * A bootstrap may be required after a call to cleanup
	 */
	void cleanup();
}
