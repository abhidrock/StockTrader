package com.netent.platform.hiring.stockTrader.api;

import java.util.Optional;

/**
 * The service interface to manage customers' Demat (Stock) Accounts
 * 
 * @author aditya.bhushan
 *
 */
public interface DematServiceIF {

	/**
	 * Allocate a number of certain {@link Stock}} to a particular user,
	 * identified by userName
	 * 
	 * @param userName
	 *            Identifier used to identify users whose stocks are being
	 *            managed by application
	 * @param stock
	 *            The {@link Stock} which is to be allocated
	 * @param quantity
	 *            Quantity of stock to allocate (positive only). If a negative
	 *            quantity is sent, an exception must be thrown.
	 * 
	 * @throws InvalidTransactionRequestException
	 *             if the quantity is negative
	 */
	public void allocateStock(String userName, Stock stock, Integer quantity)
			throws InvalidTransactionRequestException;

	/**
	 * Get the number of a particular {@link Stock} allocated to a particular
	 * user.
	 * 
	 * @param userName
	 *            Identifier used to identify users whose stocks are being
	 *            managed by application
	 * @param stock
	 *            The {@link Stock} which is to be allocated
	 * @return Number of particular stock allocated to the user
	 */
	public Optional<Integer> getStockStatus(String userName, Stock stock);
}
