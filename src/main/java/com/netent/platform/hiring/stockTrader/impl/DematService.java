/**
 *
 */
package com.netent.platform.hiring.stockTrader.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.netent.platform.hiring.stockTrader.api.DematServiceIF;
import com.netent.platform.hiring.stockTrader.api.InvalidTransactionRequestException;
import com.netent.platform.hiring.stockTrader.api.Stock;

/**
 *
 * This service is used to allocate stocks to customer and check different types
 * of stocks held by the customer.
 *
 * @author abhishek
 *
 */
public class DematService implements DematServiceIF {

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.DematServiceIF#allocateStock(java.lang.String, com.netent.platform.hiring.stockTrader.api.Stock, java.lang.Integer)
     */
    @Override
    public void allocateStock(String userName, Stock stock, Integer quantity)
            throws InvalidTransactionRequestException {
        if(quantity >= 0) {
            Optional<Integer> oldQuantity = getStockStatus(userName, stock);
            if(oldQuantity != null) {
                Integer updatedQuantity = quantity + oldQuantity.get();
                saveUserStock(userName, stock, updatedQuantity);
            } else {
                saveUserStock(userName, stock, quantity);
            }
        } else {
            throw new InvalidTransactionRequestException(
                    Constants.NEGATIVE_QUANTITY);
        }

    }

    /**
     *
     * Method to save stock in database
     * @param userName
     *                UserName of the customer
     * @param stock
     *               Stock
     * @param quantity
     *               Quantity of stock allocated to the customer
     */
    public void saveUserStock(String userName, Stock stock, Integer quantity){
        try(Connection conn = DriverManager.getConnection(Constants.DB_URL,
                Constants.USER,
                Constants.PASS);
                PreparedStatement stmt = conn.prepareStatement(
                        Constants.MERGE_STOCK)) {
                stmt.setString(1, userName);
                stmt.setString(2, stock.toString());
                stmt.setInt(3, quantity);
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.DematServiceIF#getStockStatus(java.lang.String, com.netent.platform.hiring.stockTrader.api.Stock)
     */
    @Override
    public Optional<Integer> getStockStatus(String userName, Stock stock) {
        try(Connection conn = DriverManager.getConnection(Constants.DB_URL,
                Constants.USER,
                Constants.PASS);
            PreparedStatement stmt = conn.prepareStatement(Constants.GET_STOCK)) {
            stmt.setString(1, userName);
            stmt.setString(2, stock.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                return Optional.of(rs.getInt(Constants.QUANTITY));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
