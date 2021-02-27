package algo.trade.bot.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Trade row object
 * 
 * @author Abhinav Shetty
 *
 */
public class TradeVO implements Comparable<TradeVO>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3847377202664970222L;
	
	private int id;
	private int marketId;
	private int tradeTypeId;
	private int strategyId;
	private int lifecycleId;
	private String tradeCurrency;
	private BigDecimal buyPrice;
	private Timestamp buyTime;
	private BigDecimal sellPrice;
	private Timestamp sellTime;
	private BigDecimal quantity;

	private String buyClientOrderId;
	private Long buyOrderId;
	private String sellClientOrderId;
	private Long sellOrderId;
	
	/**
	 * @param id
	 * @param tradeCurrency
	 * @param buyPrice
	 * @param buyClientOrderId
	 * @param buyOrderId
	 * @param buyTime
	 * @param sellPrice
	 * @param sellTime
	 * @param sellClientOrderId
	 * @param sellOrderId
	 * @param quantity
	 */
	public TradeVO(int id, String tradeCurrency, BigDecimal buyPrice, String buyClientOrderId,
			Long buyOrderId, Timestamp buyTime, BigDecimal sellPrice, Timestamp sellTime, String sellClientOrderId,
			Long sellOrderId, BigDecimal quantity) {
		super();
		this.id = id;
		this.tradeCurrency = tradeCurrency;
		this.buyPrice = buyPrice;
		this.buyClientOrderId = buyClientOrderId;
		this.buyOrderId = buyOrderId;
		this.buyTime = buyTime;
		this.sellPrice = sellPrice;
		this.sellTime = sellTime;
		this.sellClientOrderId = sellClientOrderId;
		this.sellOrderId = sellOrderId;
		this.quantity = quantity;
	}

	/**
	 * No Parameter constructor for dynamic generation
	 */
	public TradeVO() {
		super();
	}

	public String getTradeCurrency() {
		return tradeCurrency;
	}

	public void setTradeCurrency(String tradeCurrency) {
		this.tradeCurrency = tradeCurrency;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Timestamp getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Timestamp buyTime) {
		this.buyTime = buyTime;
	}

	public Timestamp getSellTime() {
		return sellTime;
	}

	public void setSellTime(Timestamp sellTime) {
		this.sellTime = sellTime;
	}

	public String getBuyClientOrderId() {
		return buyClientOrderId;
	}

	public void setBuyClientOrderId(String buyClientOrderId) {
		this.buyClientOrderId = buyClientOrderId;
	}

	public Long getBuyOrderId() {
		return buyOrderId;
	}
	
	public void setBuyOrderId(Long buyOrderId) {
		this.buyOrderId = buyOrderId;
	}

	public void setBuyOrderId(long buyOrderId) {
		this.buyOrderId = buyOrderId;
	}

	public String getSellClientOrderId() {
		return sellClientOrderId;
	}

	public void setSellClientOrderId(String sellClientOrderId) {
		this.sellClientOrderId = sellClientOrderId;
	}

	public Long getSellOrderId() {
		return sellOrderId;
	}

	public void setSellOrderId(Long sellOrderId) {
		this.sellOrderId = sellOrderId;
	}

	public void setSellOrderId(long sellOrderId) {
		this.sellOrderId = sellOrderId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int compareTo(TradeVO o) {
		// TODO Auto-generated method stub
		int id=((TradeVO) o).getId();
        /* For Ascending order*/
        return this.id-id;
	}

	@Override
	public String toString() {
		return "TradeVO [tradeCurrency=" + tradeCurrency + ", market=" + marketId + ", buyPrice=" + buyPrice + ", buyTime=" + buyTime
				+ ", sellPrice=" + sellPrice + ", sellTime=" + sellTime + ", quantity=" + quantity + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buyClientOrderId == null) ? 0 : buyClientOrderId.hashCode());
		result = prime * result + ((buyOrderId == null) ? 0 : buyOrderId.hashCode());
		result = prime * result + ((buyPrice == null) ? 0 : buyPrice.hashCode());
		result = prime * result + ((buyTime == null) ? 0 : buyTime.hashCode());
		result = prime * result + id;
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((sellClientOrderId == null) ? 0 : sellClientOrderId.hashCode());
		result = prime * result + ((sellOrderId == null) ? 0 : sellOrderId.hashCode());
		result = prime * result + ((sellPrice == null) ? 0 : sellPrice.hashCode());
		result = prime * result + ((sellTime == null) ? 0 : sellTime.hashCode());
		result = prime * result + ((tradeCurrency == null) ? 0 : tradeCurrency.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeVO other = (TradeVO) obj;
		if (buyClientOrderId == null) {
			if (other.buyClientOrderId != null)
				return false;
		} else if (!buyClientOrderId.equalsIgnoreCase(other.buyClientOrderId))
			return false;
		if (buyOrderId == null) {
			if (other.buyOrderId != null)
				return false;
		} else if (buyOrderId.longValue() != (other.getBuyOrderId().longValue()))
			return false;
		if (buyPrice == null) {
			if (other.buyPrice != null)
				return false;
		} else if (buyPrice.doubleValue() != other.getBuyPrice().doubleValue())
			return false;
		if (buyTime == null) {
			if (other.buyTime != null)
				return false;
		} else if (!buyTime.equals(other.buyTime))
			return false;
		if (id != other.id)
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (sellClientOrderId == null) {
			if (other.sellClientOrderId != null)
				return false;
		} else if (!sellClientOrderId.equalsIgnoreCase(other.getSellClientOrderId()))
			return false;
		if (sellOrderId == null) {
			if (other.sellOrderId != null)
				return false;
		} else if (sellOrderId.longValue() != (other.getSellOrderId().longValue()))
			return false;
		if (sellPrice == null) {
			if (other.sellPrice != null)
				return false;
		} else if (sellPrice.doubleValue() != (other.getSellPrice().doubleValue()))
			return false;
		if (sellTime == null) {
			if (other.sellTime != null)
				return false;
		} else if (!sellTime.equals(other.sellTime))
			return false;
		if (tradeCurrency == null) {
			if (other.tradeCurrency != null)
				return false;
		} else if (!tradeCurrency.equals(other.tradeCurrency))
			return false;
		return true;
	}

	public int getMarketId() {
		return marketId;
	}

	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}

	public int getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(int tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public int getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(int strategyId) {
		this.strategyId = strategyId;
	}

	public int getLifecycleId() {
		return lifecycleId;
	}

	public void setLifecycleId(int lifecycleId) {
		this.lifecycleId = lifecycleId;
	}

}
