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
	private String lifecycleName;
	private String strategyName;
	private String marketName;
	private BigDecimal initialInvestment;
	private BigDecimal currentMoney;
	private String baseCurrency;
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
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getLifecycleName() {
		return lifecycleName;
	}
	public void setLifecycleName(String lifecycleName) {
		this.lifecycleName = lifecycleName;
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
		return "BotInstance [lifecycleName=" + lifecycleName
				+ ", strategyName=" + strategyName 
				+ ", marketName=" + marketName + ", initialInvestment=" + initialInvestment + ", baseCurrency="
				+ baseCurrency + "]";
	}
}
