package algo.trade.market.beans;

import java.io.Serializable;

public class TradePostDao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6608248923267506819L;
	
	private String symbol;
	private String side;
	private String type;
	private long timestamp;
	private String quantity;
	private String quantityPrecision;
	private String price;
	private String pricePrecision;
	private String signature;
	private long recvWindow;
	
	/**
	 * To build a custom algo order to stay on order book for given time
	 * @param recvWindow : Time the order will stay on the book in milliseconds
	 */
	public TradePostDao(long recvWindow) {
		this.recvWindow = recvWindow;
	}
	
	/**
	 * To build default algo order to stay on order book for ten hours
	 */
	public TradePostDao() {
		this.recvWindow = 36000000L;
	}
	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	/**
	 * @return the side
	 */
	public String getSide() {
		return side;
	}
	/**
	 * @param side the side to set
	 */
	public void setSide(String side) {
		this.side = side;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the quantityPrecision
	 */
	public String getQuantityPrecision() {
		return quantityPrecision;
	}
	/**
	 * @param quantityPrecision the quantityPrecision to set
	 */
	public void setQuantityPrecision(String quantityPrecision) {
		this.quantityPrecision = quantityPrecision;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the pricePrecision
	 */
	public String getPricePrecision() {
		return pricePrecision;
	}
	/**
	 * @param pricePrecision the pricePrecision to set
	 */
	public void setPricePrecision(String pricePrecision) {
		this.pricePrecision = pricePrecision;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getRecvWindow() {
		return recvWindow;
	}
	public void setRecvWindow(long recvWindow) {
		this.recvWindow = recvWindow;
	}
}
