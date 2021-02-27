package algo.trade.decision.client;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import algo.trade.decision.beans.EntryDecisionQuery;
import algo.trade.decision.beans.SecondaryActionDecisionQuery;
import algo.trade.errors.PositionOpenException;
import algo.trade.transform.service.BasePositionService;

public abstract class DecisionEngine extends BasePositionService{

	protected Map<String, Object> marketConfigurationConstants;

	protected Map<String, Object> botConfigurationConstants;

	protected Map<String, Object> engineConfigurationConstants;

	public Map<String, Object> getBotConfigurationConstants() {
		if (botConfigurationConstants == null) {
			botConfigurationConstants = new ConcurrentHashMap<String, Object>();
		}
		return botConfigurationConstants;
	}

	/**
	 * add bot configuration hyperparameters here
	 */
	protected abstract void initializeBotConfigurationConstants();

	public Map<String, Object> getMarketConfigurationConstants() {
		if (marketConfigurationConstants == null) {
			marketConfigurationConstants = new ConcurrentHashMap<String, Object>();
		}
		return marketConfigurationConstants;
	}

	/**
	 * add market configuration hyperparameters here
	 */
	protected abstract void initializeMarketConfigurationConstants();
	
	public Map<String, Object> getEngineConfigurationConstants() {
		if (engineConfigurationConstants == null) {
			engineConfigurationConstants = new ConcurrentHashMap<String, Object>();
		}
		return engineConfigurationConstants;
	}
	
	/**
	 * add decision engine configuration hyperparameters here
	 */
	protected abstract void initializeEngineConfigurationConstants();

	public abstract String[] getStrategies();

	/**
	 * Defines if a long position can be initiated for this item and bot
	 * 
	 * @return true / false
	 */
	public abstract Boolean shouldBotOpenLongPosition(EntryDecisionQuery request);

	/**
	 * Defines if this long position needs an extension
	 * 
	 * @return true / false
	 */
	public abstract Boolean shouldBotExtendLongPosition(SecondaryActionDecisionQuery request);

	/**
	 * Defines if a long position needs to be stop-lossed
	 * 
	 * @return true / false
	 */
	public abstract Boolean shouldBotPerformLongStopLossAction(SecondaryActionDecisionQuery request);

	/**
	 * Defines if a short position can be initiated for this item and bot
	 * 
	 * @return true / false
	 */
	public abstract Boolean shouldBotOpenShortPosition(EntryDecisionQuery request);

	/**
	 * Defines if this short position needs an extension
	 * 
	 * @return true / false
	 */
	public abstract Boolean shouldBotExtendShortPosition(SecondaryActionDecisionQuery request);

	/**
	 * Defines if a short position needs to be stop-lossed
	 * 
	 * @return true / false
	 */
	public abstract Boolean shouldBotPerformShortStopLossAction(SecondaryActionDecisionQuery request);

	/**
	 * Gets the exit sell price in a long position
	 * 
	 * @return
	 * @throws PositionOpenException 
	 */
	public abstract BigDecimal getExitSellPrice(SecondaryActionDecisionQuery request);

	/**
	 * Gets the exit buy price in a short position
	 * 
	 * @return
	 * @throws PositionOpenException 
	 */
	public abstract BigDecimal getExitBuyPrice(SecondaryActionDecisionQuery request);
	
	/**
	 * Gets the stop-loss sell price in a long position
	 * 
	 * @return
	 * @throws PositionOpenException 
	 */
	public abstract BigDecimal getStopLossSellPrice(SecondaryActionDecisionQuery request);

	/**
	 * Gets the exit buy price in a short position
	 * 
	 * @return
	 */
	public abstract BigDecimal getStopLossBuyPrice(SecondaryActionDecisionQuery request);
}
