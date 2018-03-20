package com.netent.platform.hiring.stockTrader.api;

/**
 * The exception which may be thrown in case of any business error in the StockTrader application
 * @author aditya.bhushan
 *
 */
public class InvalidTransactionRequestException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionRequestException(String string) {
		super(string);
	}

}
