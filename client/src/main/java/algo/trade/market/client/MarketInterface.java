package algo.trade.market.client;

import java.util.List;
import java.util.Map;

import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.market.beans.HistoricalTradeVO;
import algo.trade.market.beans.Kline;
import algo.trade.market.beans.RelevantItemInfoVO;
import algo.trade.market.beans.TradePostDao;

/**
 * Generic market interface defining methods to interact with a given market
 * 
 * @author Abhinav Shetty
 *
 */
public interface MarketInterface {

	/**
	 * get the account details and print to console for the bot at time
	 * 
	 * @param bot
	 * @param serverTime
	 * @return
	 */
	public Map<Object, Object> getAccountData(BotDefinition bot, long serverTime);

	/**
	 * returns list of markets that the interface can access
	 * 
	 * @return
	 */
	public List<String> getMarkets();

	/**
	 * Gets all items traded in this market
	 * 
	 * @return
	 */
	public Map<String, RelevantItemInfoVO> getAllItemsData(BotDefinition bot);

	/**
	 * Gets kline information for given inputs from market: interval is kline time
	 * width here
	 * 
	 * @param symbol
	 * @param interval
	 * @param timePeriod in hours
	 * @param currentTime in unix milliseconds
	 * @return
	 */
	public List<Kline> getHistoricData(String symbol, String interval, int timePeriod, long currentTime);

	/**
	 * Post a trade and return the response from request.
	 * 
	 * @param leg
	 * @param headerKey
	 * @return TradePostResponseDao
	 */
	public TradeVO postTrade(TradePostDao leg, RelevantItemInfoVO tickerInfo, BotDefinition bot);

	/**
	 * Check if trade is filled for user
	 * 
	 * @param headerKey
	 * @param trade
	 * @return status is filled or not.
	 */
	public boolean isTradeFilled(BotDefinition bot, List<TradeVO> trade, long serverTime);

	/**
	 * gets server time when the bot is running
	 * 
	 * @return
	 */
	public long getServerTime(BotDefinition bot);

	/**
	 * Cancel an existing trade in market
	 * 
	 * @param bot
	 * @param trade
	 * @param serverTime
	 */
	public Boolean cancelTrade(BotDefinition bot, TradeVO trade, long serverTime);

	/**
	 * Simulates trading on the market
	 * 
	 * @param leg
	 * @param bot
	 * @return
	 */
	public TradeVO postSimTrade(TradePostDao leg, BotDefinition bot);

	/**
	 * Simulates checking a trade on the market
	 * @param bot
	 * @param leg
	 * @param serverTime
	 * @return
	 */
	public long isSimTradeFilled(BotDefinition bot, TradeVO leg, long serverTime);

	/**
	 * gets simulation time
	 * 
	 * @param bot
	 * @return
	 */
	public long getSimServerTime(BotDefinition bot);

	/**
	 * gets historical trades on the market
	 * @param symbol
	 * @param interval
	 * @param timePeriod
	 * @param currentTime
	 * @return
	 */
	public List<HistoricalTradeVO> getHistoricalTrades(String symbol, String interval, int timePeriod,
			long currentTime);
}
