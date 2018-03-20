package com.netent.platform.hiring.stockTrader.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The service interface to allow trading of Stock amongst users
 * 
 * @author aditya.bhushan
 *
 */
public interface TradingServiceIF {

	/**
	 * Execute the given List of {@linkStockTransferTransaction}. Note that
	 * total transfer of each {@link Stock} must be zero. That is, each negative
	 * transfer from one user must be matched with a positive transfer to
	 * another user.
	 * 
	 * @param transferTransactions
	 *            List of {@link StockTransferTransactionRequest} to be executed.
	 * @throws InvalidTransactionRequestException
	 *             if the transactions do not balance, or some other business
	 *             error like making a user's Stock balance as negative.
	 */
	public void executeStockTransfer(List<StockTransferTransactionRequest> transferTransactions)
			throws InvalidTransactionRequestException;
	
	
	/**
	 * Find all the {@link StockTransferTransaction} executed for a user. 
	 * Includes both incoming and outgoing Stock requests to/from the user account.
	 * 
	 * @param userName User whose transactions are to be returned
	 * @return List of transactions executed for the user
	 */
	public Optional<List<StockTransferTransactionRequest>> findTransferTransactions(String userName);
	
	/**
	 * Find all the users who own a particular Stock, and also the quantities owned by each of them.
	 * 
	 * @param stock The stock to find ownership for.
	 * @return Map of UserNames and quantity of Stock owned by them.
	 */
	public Optional<Map<String, Integer>> findUsersWithStock(Stock stock);
}
