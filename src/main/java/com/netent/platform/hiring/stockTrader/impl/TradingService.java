/**
 *
 */
package com.netent.platform.hiring.stockTrader.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import com.netent.platform.hiring.stockTrader.api.DematServiceIF;
import com.netent.platform.hiring.stockTrader.api.InvalidTransactionRequestException;
import com.netent.platform.hiring.stockTrader.api.Stock;
import com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF;
import com.netent.platform.hiring.stockTrader.api.StockTransferTransactionRequest;
import com.netent.platform.hiring.stockTrader.api.TradingServiceIF;

/**
 * This service is used to transfer Stocks between users, and also to track
 * the past trade transactions on a user’s trading account.
 * Each transfer transaction can contain multiple Stock transfers.
 *
 * @author abhishek
 *
 */
public class TradingService implements TradingServiceIF {

    private List<StockTransferTransactionRequest> transferTransaction;

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.TradingServiceIF#
     * executeStockTransfer(java.util.List)
     */
    @Override
    public void executeStockTransfer(List<StockTransferTransactionRequest>
    transferTransactions)
            throws InvalidTransactionRequestException {

        this.transferTransaction = transferTransactions;
        int sum = transferTransactions.stream().mapToInt(
                StockTransferTransactionRequest::getQuantity).sum();
        if(sum == 0) {
            transferTransactions.forEach(item -> {
                updateStocks(item);
            });
        } else {
            throw new InvalidTransactionRequestException(
                    Constants.INVALID_TRANSFER);
        }
    }

    /**
     * Update the stock transfered in database
     *
     * @param item
     *            stock transfered
     */
    private void updateStocks(StockTransferTransactionRequest item) {

        Integer updatedQuantity = item.getQuantity();
        StockTraderServiceFactoryIF serviceFactory = new StockTraderServiceFactory();
        DematServiceIF dematService = serviceFactory.dematService();

        Optional<Integer> oldQuantity = dematService.getStockStatus(item.getUserName(),
                item.getStock());
        if(oldQuantity != null && oldQuantity.get() > 0) {
            updatedQuantity = oldQuantity.get() + item.getQuantity();
        }
        try(Connection conn = DriverManager.getConnection(Constants.DB_URL,
                Constants.USER,
                Constants.PASS);
                PreparedStatement stmt = conn.prepareStatement(
                        Constants.MERGE_STOCK)) {
                stmt.setString(1, item.getUserName());
                stmt.setString(2, item.getStock().toString());
                stmt.setInt(3, updatedQuantity);
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.TradingServiceIF#
     * findTransferTransactions(java.lang.String)
     */
    @Override
    public Optional<List<StockTransferTransactionRequest>>
    findTransferTransactions(String userName) {
        List<StockTransferTransactionRequest> result = transferTransaction
        .stream()
        .filter( item -> item.getUserName().equals(userName))
        .collect(Collectors.toList());
        return Optional.of(result);
    }

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.TradingServiceIF#
     * findUsersWithStock(com.netent.platform.hiring.stockTrader.api.Stock)
     */
    @Override
    public Optional<Map<String, Integer>> findUsersWithStock(Stock stock) {
        // TODO Auto-generated method stub
        return null;
    }

}
