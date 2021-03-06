package algo.trade.market.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.errors.DataException;
import algo.trade.market.beans.ItemInfo;
import algo.trade.market.beans.Kline;
import algo.trade.transform.service.BaseService;

/**
 * Generic market interface defining methods to interact with a market
 * 
 * @author Abhinav Shetty
 *
 */
public abstract class MarketInterface extends BaseService{

	/**
	 * Random order id generator. Used for simulations
	 * 
	 * @return
	 */
	protected String getRandomOrderId() {
		BigDecimal result = new BigDecimal(Math.random());
		result = result.multiply(new BigDecimal(Math.pow(10, 8)));
		result = result.abs();
		return result.toBigInteger().toString();
	}

	/**
	 * get the account details and print to console for the bot at time
	 * @param bot
	 * @return
	 */
	public abstract List<TradeVO> getAccountData(BotDefinition bot);

	/**
	 * returns list of markets that the interface can access
	 * 
	 * @return
	 */
	public abstract List<String> getMarkets();

	/**
	 * Gets all items traded in this market
	 * 
	 * @return
	 */
	public abstract Map<String, ItemInfo> getAllItemsData(BotDefinition bot);

	/**
	 * Gets kline information for given inputs from market: interval is kline time
	 * width here
	 * 
	 * @param symbol
	 * @param interval
	 * @param timePeriod
	 *            in hours
	 * @param currentTime
	 *            in unix milliseconds
	 * @return
	 */
	public abstract List<Kline> getHistoricKlines(String symbol, String interval, int timePeriod, long currentTime);

	/**
	 * Post a trade and return the response from request.
	 * 
	 * @param price
	 * @param quantity
	 * @param position
	 * @param orderType
	 * @param tickerInfo
	 * @param bot
	 * @return
	 * @throws DataException
	 */
	public abstract TradeVO postTrade(BigDecimal price, BigDecimal quantity, String position, String orderType,
			ItemInfo tickerInfo, BotDefinition bot) throws DataException;

	/**
	 * Check if trade is filled for user
	 * 
	 * @param headerKey
	 * @param trade
	 * @return status is filled or not.
	 * @throws DataException
	 */
	public abstract boolean isTradeFilled(BotDefinition bot, TradeVO trade, long serverTime) throws DataException;

	/**
	 * gets server time when the bot is running
	 * 
	 * @return
	 */
	public abstract long getServerTime(BotDefinition bot);

	/**
	 * Cancel an existing trade in market
	 * 
	 * @param bot
	 * @param trade
	 * @param serverTime
	 */
	public abstract Boolean cancelTrade(BotDefinition bot, TradeVO trade, long serverTime);

	// /**
	// * gets historical trades on the market
	// *
	// * @param symbol
	// * @param interval
	// * @param timePeriod
	// * @param currentTime
	// * @return
	// */
	// public List<HistoricalTradeVO> getHistoricalTrades(String symbol, String
	// interval, int timePeriod,
	// long currentTime);
}
