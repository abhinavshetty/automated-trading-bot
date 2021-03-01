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
	private String marketName;
	private String positionType;
	private String strategyName;
	private String lifeCycleName;
	private String botName;
	private String tradeItem;
	private BigDecimal buyPrice;
	private Timestamp buyTime;
	private BigDecimal sellPrice;
	private Timestamp sellTime;
	private BigDecimal quantity;
	private int tradeSide;

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
		this.tradeItem = tradeCurrency;
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

	public String getTradeItem() {
		return tradeItem;
	}

	public void setTradeItem(String tradeItem) {
		this.tradeItem = tradeItem;
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

	/**
	 * @return the marketName
	 */
	public String getMarketName() {
		return marketName;
	}

	/**
	 * @param marketName the marketName to set
	 */
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	/**
	 * @return the positionType
	 */
	public String getPositionType() {
		return positionType;
	}

	/**
	 * @param positionType the positionType to set
	 */
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	/**
	 * @return the strategyName
	 */
	public String getStrategyName() {
		return strategyName;
	}

	/**
	 * @param strategyName the strategyName to set
	 */
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	/**
	 * @return the lifeCycleName
	 */
	public String getLifeCycleName() {
		return lifeCycleName;
	}

	/**
	 * @param lifeCycleName the lifeCycleName to set
	 */
	public void setLifeCycleName(String lifeCycleName) {
		this.lifeCycleName = lifeCycleName;
	}

	/**
	 * @return the botName
	 */
	public String getBotName() {
		return botName;
	}

	/**
	 * @param botName the botName to set
	 */
	public void setBotName(String botName) {
		this.botName = botName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((botName == null) ? 0 : botName.hashCode());
		result = prime * result + ((buyClientOrderId == null) ? 0 : buyClientOrderId.hashCode());
		result = prime * result + ((buyOrderId == null) ? 0 : buyOrderId.hashCode());
		result = prime * result + ((buyPrice == null) ? 0 : buyPrice.hashCode());
		result = prime * result + ((buyTime == null) ? 0 : buyTime.hashCode());
		result = prime * result + id;
		result = prime * result + ((lifeCycleName == null) ? 0 : lifeCycleName.hashCode());
		result = prime * result + ((marketName == null) ? 0 : marketName.hashCode());
		result = prime * result + ((positionType == null) ? 0 : positionType.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((sellClientOrderId == null) ? 0 : sellClientOrderId.hashCode());
		result = prime * result + ((sellOrderId == null) ? 0 : sellOrderId.hashCode());
		result = prime * result + ((sellPrice == null) ? 0 : sellPrice.hashCode());
		result = prime * result + ((sellTime == null) ? 0 : sellTime.hashCode());
		result = prime * result + ((strategyName == null) ? 0 : strategyName.hashCode());
		result = prime * result + ((tradeItem == null) ? 0 : tradeItem.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TradeVO)) {
			return false;
		}
		TradeVO other = (TradeVO) obj;
		if (botName == null) {
			if (other.botName != null) {
				return false;
			}
		} else if (!botName.equals(other.botName)) {
			return false;
		}
		if (buyClientOrderId == null) {
			if (other.buyClientOrderId != null) {
				return false;
			}
		} else if (!buyClientOrderId.equals(other.buyClientOrderId)) {
			return false;
		}
		if (buyOrderId == null) {
			if (other.buyOrderId != null) {
				return false;
			}
		} else if (!buyOrderId.equals(other.buyOrderId)) {
			return false;
		}
		if (buyPrice == null) {
			if (other.buyPrice != null) {
				return false;
			}
		} else if (!buyPrice.equals(other.buyPrice)) {
			return false;
		}
		if (buyTime == null) {
			if (other.buyTime != null) {
				return false;
			}
		} else if (!buyTime.equals(other.buyTime)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (lifeCycleName == null) {
			if (other.lifeCycleName != null) {
				return false;
			}
		} else if (!lifeCycleName.equals(other.lifeCycleName)) {
			return false;
		}
		if (marketName == null) {
			if (other.marketName != null) {
				return false;
			}
		} else if (!marketName.equals(other.marketName)) {
			return false;
		}
		if (positionType == null) {
			if (other.positionType != null) {
				return false;
			}
		} else if (!positionType.equals(other.positionType)) {
			return false;
		}
		if (quantity == null) {
			if (other.quantity != null) {
				return false;
			}
		} else if (!quantity.equals(other.quantity)) {
			return false;
		}
		if (sellClientOrderId == null) {
			if (other.sellClientOrderId != null) {
				return false;
			}
		} else if (!sellClientOrderId.equals(other.sellClientOrderId)) {
			return false;
		}
		if (sellOrderId == null) {
			if (other.sellOrderId != null) {
				return false;
			}
		} else if (!sellOrderId.equals(other.sellOrderId)) {
			return false;
		}
		if (sellPrice == null) {
			if (other.sellPrice != null) {
				return false;
			}
		} else if (!sellPrice.equals(other.sellPrice)) {
			return false;
		}
		if (sellTime == null) {
			if (other.sellTime != null) {
				return false;
			}
		} else if (!sellTime.equals(other.sellTime)) {
			return false;
		}
		if (strategyName == null) {
			if (other.strategyName != null) {
				return false;
			}
		} else if (!strategyName.equals(other.strategyName)) {
			return false;
		}
		if (tradeItem == null) {
			if (other.tradeItem != null) {
				return false;
			}
		} else if (!tradeItem.equals(other.tradeItem)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the tradeSide
	 */
	public int getTradeSide() {
		return tradeSide;
	}

	/**
	 * @param tradeSide the tradeSide to set
	 */
	public void setTradeSide(int tradeSide) {
		this.tradeSide = tradeSide;
	}

}
