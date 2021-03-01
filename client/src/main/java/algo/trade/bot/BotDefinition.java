package algo.trade.bot;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Bot definition
 * @author Abhinav Shetty
 *
 */
public class BotDefinition {
	
	private int botId;
	private String lifeCycleName;
	private String strategyName;
	private String marketName;
	private String botName;
	
	private BigDecimal initialInvestment;
	private BigDecimal currentMoney;
	private String fiatCurrency;
	private Date startTime;
	private String status;
	
	public int getBotId() {
		return botId;
	}
	public void setBotId(int botId) {
		this.botId = botId;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public BigDecimal getInitialInvestment() {
		return initialInvestment;
	}
	public void setInitialInvestment(BigDecimal initialInvestment) {
		this.initialInvestment = initialInvestment;
	}
	public String getLifeCycleName() {
		return lifeCycleName;
	}
	public void setLifeCycleName(String lifeCycleName) {
		this.lifeCycleName = lifeCycleName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public BigDecimal getCurrentMoney() {
		return currentMoney;
	}
	public void setCurrentMoney(BigDecimal currentMoney) {
		this.currentMoney = currentMoney;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "BotInstance [lifecycleName=" + lifeCycleName
				+ ", strategyName=" + strategyName 
				+ ", marketName=" + marketName + ", initialInvestment=" + initialInvestment + ", baseCurrency="
				+ fiatCurrency + "]";
	}
	public String getFiatCurrency() {
		return fiatCurrency;
	}
	public void setFiatCurrency(String fiatCurrency) {
		this.fiatCurrency = fiatCurrency;
	}
	public String getBotName() {
		return botName;
	}
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
		result = prime * result + botId;
		result = prime * result + ((botName == null) ? 0 : botName.hashCode());
		result = prime * result + ((currentMoney == null) ? 0 : currentMoney.hashCode());
		result = prime * result + ((fiatCurrency == null) ? 0 : fiatCurrency.hashCode());
		result = prime * result + ((initialInvestment == null) ? 0 : initialInvestment.hashCode());
		result = prime * result + ((lifeCycleName == null) ? 0 : lifeCycleName.hashCode());
		result = prime * result + ((marketName == null) ? 0 : marketName.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((strategyName == null) ? 0 : strategyName.hashCode());
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
		if (!(obj instanceof BotDefinition)) {
			return false;
		}
		BotDefinition other = (BotDefinition) obj;
		if (botName == null) {
			if (other.botName != null) {
				return false;
			}
		} else if (!botName.equals(other.botName)) {
			return false;
		}
		return true;
	}
}
