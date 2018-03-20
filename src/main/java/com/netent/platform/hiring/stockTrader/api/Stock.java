package com.netent.platform.hiring.stockTrader.api;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing a company Stock, identified by a stockSymbol.
 * 
 * @author aditya.bhushan
 *
 */
public final class Stock implements Serializable {

	private static final long serialVersionUID = -6236330594233076607L;

	private String stockSymbol;

	public Stock(String stockSymbol) {
		this.stockSymbol = Objects.requireNonNull(stockSymbol, "Stock Symbol cannot be null").toLowerCase();
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		return Objects.equals(stockSymbol, other.stockSymbol);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stockSymbol == null) ? 0 : stockSymbol.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return stockSymbol;
	}

}
