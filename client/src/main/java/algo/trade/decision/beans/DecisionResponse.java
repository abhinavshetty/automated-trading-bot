package algo.trade.decision.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class DecisionResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8359819013292701538L;
	
	private boolean shouldBotActOnItem;
	private Map<String, BigDecimal> decisionParameters;
	private Map<String, Object> configParameters;
	
	public boolean isShouldBotActOnItem() {
		return shouldBotActOnItem;
	}
	public void setShouldBotActOnItem(boolean shouldBotActOnItem) {
		this.shouldBotActOnItem = shouldBotActOnItem;
	}
	public Map<String, BigDecimal> getDecisionParameters() {
		return decisionParameters;
	}
	public void setDecisionParameters(Map<String, BigDecimal> decisionParameters) {
		this.decisionParameters = decisionParameters;
	}
	public Map<String, Object> getConfigParameters() {
		return configParameters;
	}
	public void setConfigParameters(Map<String, Object> configParameters) {
		this.configParameters = configParameters;
	}
}
