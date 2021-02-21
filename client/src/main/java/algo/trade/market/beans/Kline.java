package algo.trade.market.beans;

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
	protected double open;
	protected double close;
	protected double amplitude;
	protected double percentChange;
	protected double high;
	protected double low;
	protected double volume;
	protected int numberOfTrades;
	protected double takerBuyBaseAssetVolume;
	protected double takerBuyQuoteAssetVolume;
	
	/**
	 * @param open
	 * @param close
	 * @param amplitude
	 * @param percentChange
	 * @param high
	 * @param low
	 * @param volume
	 */
	public Kline(long openTime, double open, double close, double amplitude, double percentChange, double high, double low,
			double volume) {
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

	public double getOpen() {
		return open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	
	public double getPercentChange() {
		return (close - open) / close;
	}
	public void setPercentChange(double percentChange) {
		this.percentChange = percentChange;
	}
	public double getAmplitude() {
		return (high - low) / low;
	}
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
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

	public double getTakerBuyBaseAssetVolume() {
		return takerBuyBaseAssetVolume;
	}

	public void setTakerBuyBaseAssetVolume(double takerBuyBaseAssetVolume) {
		this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
	}

	public double getTakerBuyQuoteAssetVolume() {
		return takerBuyQuoteAssetVolume;
	}

	public void setTakerBuyQuoteAssetVolume(double takerBuyQuoteAssetVolume) {
		this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
	}

	public long getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(long closeTime) {
		this.closeTime = closeTime;
	}
}
