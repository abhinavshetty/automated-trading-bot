package algo.trade.market.beans;

import java.math.BigDecimal;

public class Kline implements Comparable<Kline> {
	protected long openTime;
	protected String itemName;
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	protected long closeTime;
	protected BigDecimal open;
	protected BigDecimal close;
	protected BigDecimal amplitude;
	protected BigDecimal percentChange;
	protected BigDecimal high;
	protected BigDecimal low;
	protected BigDecimal volume;
	protected int numberOfTrades;
	protected BigDecimal takerBuyBaseAssetVolume;
	protected BigDecimal takerBuyQuoteAssetVolume;
	
	/**
	 * @param open
	 * @param close
	 * @param amplitude
	 * @param percentChange
	 * @param high
	 * @param low
	 * @param volume
	 */
	public Kline(long openTime, BigDecimal open, BigDecimal close, BigDecimal amplitude, BigDecimal percentChange, BigDecimal high, BigDecimal low,
			BigDecimal volume) {
		super();
		this.openTime = openTime;
		this.open = open;
		this.close = close;
		this.amplitude = amplitude;
		this.percentChange = percentChange;
		this.high = high;
		this.low = low;
		this.volume = volume;
	}
	
	public int compareTo(Kline o) {
		// TODO Auto-generated method stub
		long openTime=((Kline) o).getOpenTime();
        /* For Ascending order*/
        return (int)(this.openTime-openTime);
	}
	
	public Kline() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BigDecimal getOpen() {
		return open;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	
	public BigDecimal getPercentChange() {
		return (close.subtract(open)).divide(close);
	}
	public void setPercentChange(BigDecimal percentChange) {
		this.percentChange = percentChange;
	}
	public BigDecimal getAmplitude() {
		return (high.subtract(low)).divide(open);
	}
	public void setAmplitude(BigDecimal amplitude) {
		this.amplitude = amplitude;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	
	@Override
	public String toString() {
		return "Open : " + this.open + " | Close : " + this.close + " | Low : " + this.low;
	}

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public int getNumberOfTrades() {
		return numberOfTrades;
	}

	public void setNumberOfTrades(int numberOfTrades) {
		this.numberOfTrades = numberOfTrades;
	}

	public BigDecimal getTakerBuyBaseAssetVolume() {
		return takerBuyBaseAssetVolume;
	}

	public void setTakerBuyBaseAssetVolume(BigDecimal takerBuyBaseAssetVolume) {
		this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
	}

	public BigDecimal getTakerBuyQuoteAssetVolume() {
		return takerBuyQuoteAssetVolume;
	}

	public void setTakerBuyQuoteAssetVolume(BigDecimal takerBuyQuoteAssetVolume) {
		this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
	}

	public long getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(long closeTime) {
		this.closeTime = closeTime;
	}
}
