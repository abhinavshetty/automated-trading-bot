package algo.trade.decision.client;

import algo.trade.decision.beans.SecondaryActionDecisionQuery;
import algo.trade.decision.beans.EntryDecisionQuery;

public interface DecisionEngine {
	
	public String[] getStrategies();
	
	/**
	 * Defines if a long position can be initiated for this item and bot
	 * @return true / false
	 */
	public Boolean shouldBotOpenLongPosition(EntryDecisionQuery request);
	
	/**
	 * Defines if this long position needs an extension
	 * @return true / false
	 */
	public Boolean shouldBotExtendLongPosition(SecondaryActionDecisionQuery request);
	
	/**
	 * Defines if a long position needs to be stop-lossed
	 * @return true / false
	 */
	public Boolean shouldBotPerformLongStopLossAction(SecondaryActionDecisionQuery request);
	
	/**
	 * Defines if a short position can be initiated for this item and bot
	 * @return true / false
	 */
	public Boolean shouldBotOpenShortPosition(EntryDecisionQuery request);
	
	/**
	 * Defines if this short position needs an extension
	 * @return true / false
	 */
	public Boolean shouldBotExtendShortPosition(SecondaryActionDecisionQuery request);

	/**
	 * Defines if a short position needs to be stop-lossed
	 * @return true / false
	 */
	public Boolean shouldBotPerformShortStopLossAction(SecondaryActionDecisionQuery request);
	
	/**
	 * Gets the exit sell price in a long position
	 * @return
	 */
	public Double getExitSellPrice(SecondaryActionDecisionQuery request);
	
	/**
	 * Gets the exit buy price in a short position
	 * @return
	 */
	public Double getExitBuyPrice(SecondaryActionDecisionQuery request);
}
