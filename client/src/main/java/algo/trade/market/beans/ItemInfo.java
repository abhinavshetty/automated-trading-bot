package algo.trade.market.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Contains information relevant to trading perspective
 * use currentlyInTrade to track currency execution
 * @author Abhinav
 *
 */
public class ItemInfo implements Serializable {
	
	private BigDecimal monthlyTradeVolume;
	
	public int compareTo(ItemInfo o) {
		// TODO Auto-generated method stub
		BigDecimal vol=((ItemInfo) o).getMonthlyTradeVolume();
        /* For Ascending order*/
        return this.monthlyTradeVolume.subtract(vol).intValue();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2081277106487255913L;

	/**
	 * Default Constructor
	 */
	public ItemInfo() {
		super();
		// TODO Auto-generated constructor stub
		this.monthlyTradeVolume = BigDecimal.ZERO;
	}
	/**
	 * @param symbol
	 * @param pricePrecision
	 * @param quantityPrecision
	 * @param currentlyInTrade
	 */
	public ItemInfo(ItemInfo ticker) {
		super();
		this.symbol = ticker.getSymbol();
		this.pricePrecision = ticker.getPricePrecision();
		this.quantityPrecision = ticker.getQuantityPrecision();
		this.currentlyInTrade = ticker.isCurrentlyInTrade();
		this.lockStart = new Long(0);
		this.monthlyTradeVolume = BigDecimal.ZERO;
	}
	
	private String symbol;
	private Integer pricePrecision;
	private Integer quantityPrecision;
	private Boolean currentlyInTrade;
	private Long lockStart;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getPricePrecision() {
		return pricePrecision;
	}
	public void setPricePrecision(int pricePrecision) {
		this.pricePrecision = pricePrecision;
	}
	public int getQuantityPrecision() {
		return quantityPrecision;
	}
	public void setQuantityPrecision(int quantityPrecision) {
		this.quantityPrecision = quantityPrecision;
	}
	public boolean isCurrentlyInTrade() {
		return currentlyInTrade;
	}
	public boolean isCurrentlyInTrade(long currentTime) {
		if (this.lockStart == 0) {
			return this.currentlyInTrade;
		} else {
			this.currentlyInTrade = currentTime - this.lockStart < 60000;
			this.lockStart = currentTime - this.lockStart < 60000 ? this.lockStart : 0;
			return this.currentlyInTrade;
		}
	}
	public void setCurrentlyInTrade(boolean currentlyInTrade) {
		this.currentlyInTrade = currentlyInTrade;
	}
	
	public String toString() {
		return this.symbol + " | " + this.pricePrecision + " | " + this.quantityPrecision;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pricePrecision;
		result = prime * result + quantityPrecision;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemInfo other = (ItemInfo) obj;
		if (pricePrecision != other.pricePrecision)
			return false;
		if (quantityPrecision != other.quantityPrecision)
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	/**
	 * @return the lockStart
	 */
	public long getLockStart() {
		return lockStart;
	}
	/**
	 * @param lockStart the lockStart to set
	 */
	public void setLockStart(long lockStart) {
		this.lockStart = lockStart;
	}
	public BigDecimal getMonthlyTradeVolume() {
		return monthlyTradeVolume;
	}
	public void setMonthlyTradeVolume(BigDecimal monthlyTradeVolume) {
		this.monthlyTradeVolume = monthlyTradeVolume;
	}
}
