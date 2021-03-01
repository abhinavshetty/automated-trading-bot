package algo.trade.lifecycle.client;

import java.util.List;
import java.util.Map;

import algo.trade.bot.BotDefinition;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.market.beans.Kline;
import algo.trade.market.beans.ItemInfo;

/**
 * Market data template wrapper for strategies.
 * @author Abhinav Shetty
 *
 */
public interface MarketDataWrapper {
	
	/**
	 * get unique identifiers for life cycles which this data wrapper can be used
	 * @return strategies list applicable
	 */
	public List<String> getLifeCycles();

	/**
	 * get market data to decide if a long position can be initiated
	 * @return marketData
	 * @throws MarketDoesNotExistException 
	 */
	public Map<String, List<Kline>> getLongEntryMarketData(ItemInfo tickerInfo, BotDefinition bot, long serverTime, Map<String, Object> config) throws MarketDoesNotExistException;
	
	/**
	 * get market data to decide if a short position can be initiated
	 * @return marketData
	 * @throws MarketDoesNotExistException 
	 */
	public Map<String, List<Kline>> getShortEntryMarketData(ItemInfo tickerInfo, BotDefinition bot, long serverTime, Map<String, Object> config) throws MarketDoesNotExistException;

	/**
	 * get market data to decide if a corrective action needs to be taken in long position
	 * @return marketData
	 * @throws MarketDoesNotExistException 
	 */
	public Map<String, List<Kline>> getLongCorrectionMarketData(ItemInfo tickerInfo, BotDefinition bot, long serverTime, Map<String, Object> config) throws MarketDoesNotExistException;

	/**
	 * get market data to decide if a corrective action needs to be taken in short position
	 * @return marketData
	 * @throws MarketDoesNotExistException 
	 */
	public Map<String, List<Kline>> getShortCorrectionMarketData(ItemInfo tickerInfo, BotDefinition bot, long serverTime, Map<String, Object> config) throws MarketDoesNotExistException;
}
