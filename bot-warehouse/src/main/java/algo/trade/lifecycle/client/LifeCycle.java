package algo.trade.lifecycle.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.decision.beans.DecisionResponse;
import algo.trade.decision.client.DecisionEngineClient;
import algo.trade.errors.DataException;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.errors.PositionOpenException;
import algo.trade.errors.ZeroQuantityOrderedException;
import algo.trade.factory.MarketDataWrapperFactory;
import algo.trade.factory.MarketFactory;
import algo.trade.market.beans.ItemInfo;
import algo.trade.market.beans.Kline;
import algo.trade.transform.service.BasePositionService;

/**
 * Base life cycle
 * 
 * @author Abhinav Shetty
 *
 */
public abstract class LifeCycle extends BasePositionService {

	@Autowired
	protected MarketFactory marketFactory;

	@Autowired
	protected MarketDataWrapperFactory dataFactory;

	@Autowired
	protected DecisionEngineClient engineClient;

	/**
	 * returns unique identifier for implementation
	 * 
	 * @return
	 */
	public abstract String getLifeCycle();

	/**
	 * Starts monitor for the item
	 * 
	 * @param tickerInfo
	 * @param bot
	 * @return DecisionResponse
	 * @throws MarketDoesNotExistException 
	 * @throws DataException 
	 * @throws PositionOpenException 
	 */
	public abstract DecisionResponse performMonitorOperationForItem(ItemInfo itemInfo, BotDefinition bot,
			List<TradeVO> openPositions, Map<String, Object> config) throws MarketDoesNotExistException, DataException, PositionOpenException;

	/**
	 * Enters into a long position for the item
	 * 
	 * @param bot
	 * @param itemInfo
	 * @param buyCash
	 * @param marketData
	 * @param currentTime
	 * @return
	 * @throws ZeroQuantityOrderedException 
	 * @throws MarketDoesNotExistException 
	 * @throws DataException 
	 */
	public abstract TradeVO enterLongPositionAndPostExitTrade(BotDefinition bot, ItemInfo itemInfo,
			Map<String, List<Kline>> marketData, long currentTime, Map<String, Object> config) throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException;

	/**
	 * Enters into a short position for the item
	 * 
	 * @param bot
	 * @param itemInfo
	 * @param buyCash
	 * @param marketData
	 * @param currentTime
	 * @return
	 * @throws MarketDoesNotExistException 
	 * @throws DataException 
	 */
	public abstract TradeVO enterShortPositionAndPostExitTrade(BotDefinition bot, ItemInfo itemInfo, 
			Map<String, List<Kline>> marketData, long currentTime, Map<String, Object> config) throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException;

	/**
	 * Extends long position in an item
	 * 
	 * @param itemInfo
	 * @param currentPosition
	 * @param marketData
	 * @param serverTime
	 * @param bot
	 * @return
	 * @throws ZeroQuantityOrderedException 
	 * @throws MarketDoesNotExistException 
	 * @throws DataException 
	 */
	public abstract TradeVO extendLongPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long serverTime, BotDefinition bot, Map<String, Object> config) throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException;

	/**
	 * Extends short position into item
	 * 
	 * @param itemInfo
	 * @param currentPosition
	 * @param marketData
	 * @param serverTime
	 * @param bot
	 * @return
	 * @throws ZeroQuantityOrderedException 
	 * @throws MarketDoesNotExistException 
	 * @throws DataException 
	 */
	public abstract TradeVO extendShortPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long serverTime, BotDefinition bot, Map<String, Object> config) throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException;

	/**
	 * Stop losses a long position in an item
	 * 
	 * @param itemInfo
	 * @param currentPosition
	 * @param marketData
	 * @param serverTime
	 * @param bot
	 * @return
	 * @throws MarketDoesNotExistException 
	 * @throws DataException 
	 */
	public abstract TradeVO stopLossLongPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long serverTime, BotDefinition bot, Map<String, Object> config) throws MarketDoesNotExistException, DataException;

	/**
	 * Stop losses a short position in an item
	 * 
	 * @param itemInfo
	 * @param currentPosition
	 * @param marketData
	 * @param serverTime
	 * @param bot
	 * @return
	 * @throws MarketDoesNotExistException 
	 * @throws DataException 
	 */
	public abstract TradeVO stopLossShortPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long serverTime, BotDefinition bot, Map<String, Object> config) throws MarketDoesNotExistException, DataException;

}
