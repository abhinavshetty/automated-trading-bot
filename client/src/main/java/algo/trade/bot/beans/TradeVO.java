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
	private BigDecimal tradePrice;
	private Timestamp tradeTime;
	private BigDecimal quantity;
	private int tradeType;

	private String tradeClientOrderId;
	private Long tradeOrderId;

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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
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

	/**
	 * @return the tradePrice
	 */
	public BigDecimal getTradePrice() {
		return tradePrice;
	}

	/**
	 * @param tradePrice the tradePrice to set
	 */
	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}

	/**
	 * @return the tradeTime
	 */
	public Timestamp getTradeTime() {
		return tradeTime;
	}

	/**
	 * @param tradeTime the tradeTime to set
	 */
	public void setTradeTime(Timestamp tradeTime) {
		this.tradeTime = tradeTime;
	}

	/**
	 * @return the tradeClientOrderId
	 */
	public String getTradeClientOrderId() {
		return tradeClientOrderId;
	}

	/**
	 * @param tradeClientOrderId the tradeClientOrderId to set
	 */
	public void setTradeClientOrderId(String tradeClientOrderId) {
		this.tradeClientOrderId = tradeClientOrderId;
	}

	/**
	 * @return the tradeOrderId
	 */
	public Long getTradeOrderId() {
		return tradeOrderId;
	}

	/**
	 * @param tradeOrderId the tradeOrderId to set
	 */
	public void setTradeOrderId(Long tradeOrderId) {
		this.tradeOrderId = tradeOrderId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((botName == null) ? 0 : botName.hashCode());
		result = prime * result + ((lifeCycleName == null) ? 0 : lifeCycleName.hashCode());
		result = prime * result + ((marketName == null) ? 0 : marketName.hashCode());
		result = prime * result + ((positionType == null) ? 0 : positionType.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((strategyName == null) ? 0 : strategyName.hashCode());
		result = prime * result + ((tradeClientOrderId == null) ? 0 : tradeClientOrderId.hashCode());
		result = prime * result + ((tradeItem == null) ? 0 : tradeItem.hashCode());
		result = prime * result + ((tradeOrderId == null) ? 0 : tradeOrderId.hashCode());
		result = prime * result + ((tradePrice == null) ? 0 : tradePrice.hashCode());
		result = prime * result + tradeType;
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
		if (strategyName == null) {
			if (other.strategyName != null) {
				return false;
			}
		} else if (!strategyName.equals(other.strategyName)) {
			return false;
		}
		if (tradeClientOrderId == null) {
			if (other.tradeClientOrderId != null) {
				return false;
			}
		} else if (!tradeClientOrderId.equals(other.tradeClientOrderId)) {
			return false;
		}
		if (tradeItem == null) {
			if (other.tradeItem != null) {
				return false;
			}
		} else if (!tradeItem.equals(other.tradeItem)) {
			return false;
		}
		if (tradeOrderId == null) {
			if (other.tradeOrderId != null) {
				return false;
			}
		} else if (!tradeOrderId.equals(other.tradeOrderId)) {
			return false;
		}
		if (tradePrice == null) {
			if (other.tradePrice != null) {
				return false;
			}
		} else if (!tradePrice.equals(other.tradePrice)) {
			return false;
		}
		if (tradeType != other.tradeType) {
			return false;
		}
		return true;
	}

	/**
	 * @return the tradeType
	 */
	public int getTradeType() {
		return tradeType;
	}

	/**
	 * @param tradeType the tradeType to set
	 */
	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

}
