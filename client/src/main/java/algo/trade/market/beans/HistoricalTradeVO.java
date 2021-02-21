package algo.trade.market.beans;

public class HistoricalTradeVO {
	private String tradeCurrency;
	private long tradeTime;
	private boolean wasBuyerMaker;
	private boolean wasTradeBestMatch;
	private double price;
	private double quantity;
	private int id;
	
	public String getTradeCurrency() {
		return tradeCurrency;
	}
	public void setTradeCurrency(String tradeCurrency) {
		this.tradeCurrency = tradeCurrency;
	}
	public long getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(long tradeTime) {
		this.tradeTime = tradeTime;
	}
	public boolean isWasBuyerMaker() {
		return wasBuyerMaker;
	}
	public void setWasBuyerMaker(boolean wasBuyerMaker) {
		this.wasBuyerMaker = wasBuyerMaker;
	}
	public boolean isWasTradeBestMatch() {
		return wasTradeBestMatch;
	}
	public void setWasTradeBestMatch(boolean wasTradeBestMatch) {
		this.wasTradeBestMatch = wasTradeBestMatch;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
