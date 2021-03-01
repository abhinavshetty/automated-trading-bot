package algo.trade.bot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;

import algo.trade.bot.beans.TradeVO;
import algo.trade.decision.client.EngineClient;
import algo.trade.errors.LifeCycleDoesNotExistException;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.factory.LifecycleFactory;
import algo.trade.factory.MarketFactory;
import algo.trade.market.beans.ItemInfo;
import algo.trade.transform.service.BaseService;

/**
 * generically defines a bot
 * @author Abhinav Shetty
 *
 */
public abstract class Bot extends BaseService implements Callable<Boolean>{
	
	@Autowired
	protected MarketFactory marketFactory;

	@Autowired
	protected LifecycleFactory lifecycleFactory;
	
	@Autowired
	protected EngineClient engine;
	
	protected Map<String, ItemInfo> botMarket;
	
	protected Map<String, Object> botConfigurationConstants;
	
	protected List<TradeVO> outstandingBotPositions;
	
	protected BotDefinition botDefinition;
	
	/**
	 * initializes the bot.
	 * Includes the markets and open positions of operational bots
	 * @return
	 * @throws MarketDoesNotExistException 
	 */
	public abstract Boolean setupBot() throws MarketDoesNotExistException;
		
	/**
	 * Starts the bot activity
	 * @throws LifeCycleDoesNotExistException 
	 * @throws MarketDoesNotExistException 
	 */
	public abstract void startBot() throws LifeCycleDoesNotExistException, MarketDoesNotExistException;
	
	/**
	 * Stops the bot activity
	 */
	public abstract void stopBot();
	
	/**
	 * Returns the unique identifier for this bot
	 * @return
	 */
	public BotDefinition getBotDefinition() {
		return this.botDefinition;
	}
	
	public abstract String getBotName();
	
	/**
	 * @throws MarketDoesNotExistException 
	 * @throws LifeCycleDoesNotExistException 
	 * 
	 */
	@Override
	public Boolean call() throws MarketDoesNotExistException, LifeCycleDoesNotExistException {
		setupBot();
		startBot();
		return true;
	}

	protected void setBotMarket(Map<String, ItemInfo> botMarket) {
		this.botMarket = botMarket;
	}

	protected void setOutstandingBotPositions(List<TradeVO> outstandingBotPositions) {
		this.outstandingBotPositions = outstandingBotPositions;
	}

	protected void setBotDefinition(BotDefinition botDefinition) {
		this.botDefinition = botDefinition;
	}

	protected void setBotConfigurationConstants(Map<String, Object> botConfigurationConstants) {
		this.botConfigurationConstants = botConfigurationConstants;
	}

}
