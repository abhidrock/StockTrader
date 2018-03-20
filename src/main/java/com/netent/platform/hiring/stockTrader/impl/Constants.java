package com.netent.platform.hiring.stockTrader.impl;

public class Constants {
    // JDBC driver name and database URL
    public static final String DB_URL = "jdbc:h2:~/test";
    //  Database credentials
    public static final String USER = "sa";
    public static final String PASS = "";

    public static final String INSERT_STOCK = "INSERT INTO STOCK VALUES(?, ?, ?)";
    public static final String MERGE_STOCK = "MERGE INTO STOCK KEY (username, stock) VALUES (?, ?, ?)";
    public static final String GET_STOCK = "SELECT quantity FROM STOCK WHERE username=? AND stock=?";
    public static final String QUANTITY = "quantity";
    public static final String NEGATIVE_QUANTITY = "Quantity of stock can't be negative";
    public static String INVALID_TRANSFER = "Invalid transfer vales";
}
