package algo.trade.bot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import algo.trade.bot.beans.TradeVO;
import algo.trade.decision.client.DecisionEngineClient;
import algo.trade.errors.DataException;
import algo.trade.errors.LifeCycleDoesNotExistException;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.errors.PositionOpenException;
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
	
	protected MarketFactory marketFactory;

	protected LifecycleFactory lifecycleFactory;
	
	protected DecisionEngineClient engine;
	
	protected Map<String, ItemInfo> botMarket;
	
	protected Map<String, Object> botConfigurationConstants;
	
	protected List<TradeVO> outstandingBotPositions;
	
	protected BotDefinition botDefinition;

	public Bot() {
		super();
		LOGGER();
		// TODO Auto-generated constructor stub
	}

	/**
	 * instantiates the bot with the necessary resources.
	 * @param marketFactory
	 * @param lifecycleFactory
	 * @param engine
	 * @param botDefinition
	 */
	public void instantiateBot(MarketFactory marketFactory, LifecycleFactory lifecycleFactory, DecisionEngineClient engine,
			BotDefinition botDefinition) {
		this.marketFactory = marketFactory;
		this.lifecycleFactory = lifecycleFactory;
		this.engine = engine;
		this.botDefinition = botDefinition;
	}

	/**
	 * initializes the bot.
	 * Includes the markets and open positions of operational bots
	 * @return
	 * @throws MarketDoesNotExistException 
	 */
	public abstract Boolean setupBot();
		
	/**
	 * Starts the bot activity
	 */
	public abstract void startBot();
	
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
	 * @throws PositionOpenException 
	 * @throws DataException 
	 * 
	 */
	@Override
	public Boolean call() {
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

	public void setBotDefinition(BotDefinition botDefinition) {
		this.botDefinition = botDefinition;
	}

	protected void setBotConfigurationConstants(Map<String, Object> botConfigurationConstants) {
		this.botConfigurationConstants = botConfigurationConstants;
	}

}
