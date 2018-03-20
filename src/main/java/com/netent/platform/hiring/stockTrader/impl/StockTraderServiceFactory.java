package com.netent.platform.hiring.stockTrader.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.netent.platform.hiring.stockTrader.api.DematServiceIF;
import com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF;
import com.netent.platform.hiring.stockTrader.api.TradingServiceIF;

/**
 * Factory class to get the tradingService and DematService instance.
 * @author abhishek
 *
 */
public class StockTraderServiceFactory implements StockTraderServiceFactoryIF{


    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF#tradingService()
     */
    @Override
    public TradingServiceIF tradingService() {
        return new TradingService();
    }

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF#dematService()
     */
    @Override
    public DematServiceIF dematService() {
        return new DematService();
    }

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF#bootstrap()
     */
    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF#bootstrap()
     */
    @Override
    public void bootstrap() {
        createTable();
    }

    /**
     * Method to create table stock to save user stocks quantity
     */
    private void createTable() {
        StringBuilder query = new StringBuilder()
                .append("CREATE TABLE STOCK")
                .append("(username VARCHAR(255) not NULL, ")
                .append("stock VARCHAR(255) not NULL, ")
                .append("quantity INTEGER, ")
                .append("PRIMARY KEY (username, stock))");
        String sql = query.toString();
        try(Connection conn = DriverManager.getConnection(Constants.DB_URL,
                Constants.USER,
                Constants.PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see com.netent.platform.hiring.stockTrader.api.StockTraderServiceFactoryIF#cleanup()
     */
    @Override
    public void cleanup() {
        dropTable();
    }

    /**
     * Method to drop table stock
     */
    private void dropTable() {
        String sql = "DROP TABLE STOCK";
        try(Connection conn = DriverManager.getConnection(Constants.DB_URL,
                Constants.USER,
                Constants.PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
