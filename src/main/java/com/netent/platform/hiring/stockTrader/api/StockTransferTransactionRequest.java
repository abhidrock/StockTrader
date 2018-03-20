package com.netent.platform.hiring.stockTrader.api;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class represents a request for transfer of
 * {@link Stock} from/to a user using TradingService. The transfer could be
 * positive or negative.
 * 
 * @author aditya.bhushan
 *
 */
public final class StockTransferTransactionRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private Stock stock;
	private Integer quantity; // can be positive or negative

	private StockTransferTransactionRequest() {
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private StockTransferTransactionRequest transaction = new StockTransferTransactionRequest();

		public Builder user(String userName) {
			transaction.userName = Objects.requireNonNull(userName, "UserName is mandatory");
			return this;
		}

		public Builder stock(Stock stock) {
			transaction.stock = Objects.requireNonNull(stock, "Stock is mandatory");
			return this;
		}

		public Builder quantity(Integer quantity) {
			transaction.quantity = Objects.requireNonNull(quantity, "Quantity is mandatory");
			return this;
		}

		public StockTransferTransactionRequest build() throws InvalidTransactionRequestException {
			if (Stream.of(transaction.userName, transaction.stock, transaction.quantity).anyMatch(Objects::isNull)) {
				throw new InvalidTransactionRequestException("StockTransferTransaction not fully populated");
			}
			return transaction;
		}
	}

	@Override
	public String toString() {
		return "StockTransferTransaction [userName=" + userName + ", stock=" + stock + ", quantity=" + quantity + "]";
	}

	public String getUserName() {
		return userName;
	}

	public Stock getStock() {
		return stock;
	}

	public Integer getQuantity() {
		return quantity;
	}

}
