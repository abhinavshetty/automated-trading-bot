package algo.trade.lifecycle.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.constants.SystemConstants;
import algo.trade.decision.beans.DecisionResponse;
import algo.trade.errors.DataException;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.errors.PositionOpenException;
import algo.trade.errors.ZeroQuantityOrderedException;
import algo.trade.lifecycle.client.LifeCycle;
import algo.trade.lifecycle.client.MarketDataWrapper;
import algo.trade.market.beans.ItemInfo;
import algo.trade.market.beans.Kline;
import algo.trade.market.client.MarketInterface;

/**
 * Cost averaging life cycle. Uses market trades to enter and cumulative limit
 * trades to exit a position.
 * 
 * @author Abhinav Shetty
 */
@Service
public class CostAveragedLifeCycle extends LifeCycle {

	private static final String LIFECYCLE = "CA";

	@Override
	public String getLifeCycle() {
		return LIFECYCLE;
	}

	@Override
	public DecisionResponse performMonitorOperationForItem(ItemInfo itemInfo, BotDefinition bot,
			List<TradeVO> allOpenPositions, Map<String, Object> config)
			throws MarketDoesNotExistException, DataException, PositionOpenException {
		// monitors an item for decision engine
		DecisionResponse result = null;
		List<TradeVO> openPositionsForItem = allOpenPositions;
		openPositionsForItem.removeIf(item -> !(itemInfo.getSymbol().equalsIgnoreCase(item.getTradeItem())));

		MarketInterface marketClient = marketFactory.getClient(bot);
		MarketDataWrapper dataWrapper = dataFactory.getDataWrapperForLifeCycle(bot);

		Long serverTime = marketClient.getServerTime(bot);

		if (openPositionsForItem.size() == 0) {
			// this has no open positions, evaluate entry possibilities

			Map<String, List<Kline>> entryData = dataWrapper.getLongEntryMarketData(itemInfo, bot, serverTime, config);
			DecisionResponse longCondition = engineClient.shouldBotOpenLongPosition(itemInfo, entryData, bot,
					serverTime);

			if (longCondition.isShouldBotActOnItem()) {
				result = longCondition;
			} else {
				DecisionResponse shortCondition = engineClient.shouldBotOpenShortPosition(itemInfo, entryData, bot,
						serverTime);
				if (shortCondition.isShouldBotActOnItem()) {
					result = shortCondition;
				}
			}

			entryData = null;
			longCondition = null;
		} else {
			// there are open positions for this item. Evaluate stop-loss or extension
			// possibilities
			if (SystemConstants.LONG_POSITION.equalsIgnoreCase(openPositionsForItem.get(0).getPositionType())) {
				// open long position: check for close followed by stop loss followed by
				// extension

				if (hasPositionBeenClosed(allOpenPositions, itemInfo, bot)) {
					LOG.info("The long position in " + itemInfo.getSymbol() + " was closed successfully at a profit.");
					for (TradeVO trade : openPositionsForItem) {
						trade.setIsPositionClosed(true);
						if (trade.getTradeTime() == null) {
							trade.setTradeTime(new Timestamp(serverTime));
						}
					}
					LOG.info("Trade profit : " + this.getTotalReturnFromTradesInList(openPositionsForItem));
					result = constructEmptyResponse();
					result.setShouldBotActOnItem(true);
					result.setConfigParameters(new ConcurrentHashMap<String, Object>());
					result.getConfigParameters().put(SystemConstants.ITEM_INFO_KEY, itemInfo);
					result.getConfigParameters().put(SystemConstants.DECISION_TAKING_ACTION_KEY, SystemConstants.POSITION_CLOSE_ACTION);
//					result.set
				} else {
					Map<String, List<Kline>> correctionData = dataWrapper.getLongCorrectionMarketData(itemInfo, bot,
							serverTime, config);

					DecisionResponse stopLossLongCondition = engineClient.shouldBotPerformLongStopLossAction(itemInfo,
							correctionData, bot, openPositionsForItem, serverTime);

					if (stopLossLongCondition.isShouldBotActOnItem()) {
						// this item needs to be stop-lossed
						result = stopLossLongCondition;
					} else {
						// perform extension action
						DecisionResponse extendLongCondition = engineClient.shouldBotExtendLongPosition(itemInfo,
								correctionData, bot, openPositionsForItem, serverTime);
						if (extendLongCondition.isShouldBotActOnItem()) {
							// this item needs to be stop-lossed
							result = extendLongCondition;
						}
						extendLongCondition = null;
					}
					stopLossLongCondition = null;
					correctionData = null;
				}

			} else {
				// open short position: check for close check for stop loss followed by
				// extension
				if (hasPositionBeenClosed(allOpenPositions, itemInfo, bot)) {
					LOG.info("The short position in " + itemInfo.getSymbol() + " was closed successfully at a profit.");
					for (TradeVO trade : openPositionsForItem) {
						trade.setIsPositionClosed(true);
						if (trade.getTradeTime() == null) {
							trade.setTradeTime(new Timestamp(serverTime));
						}
					}
					LOG.info("Trade profit : " + this.getTotalReturnFromTradesInList(openPositionsForItem));
					result = constructEmptyResponse();
					result.setShouldBotActOnItem(true);
					result.setConfigParameters(new ConcurrentHashMap<String, Object>());
					result.getConfigParameters().put(SystemConstants.ITEM_INFO_KEY, itemInfo);
					result.getConfigParameters().put(SystemConstants.DECISION_TAKING_ACTION_KEY, SystemConstants.POSITION_CLOSE_ACTION);
				} else {
					Map<String, List<Kline>> correctionData = dataWrapper.getShortCorrectionMarketData(itemInfo, bot,
							serverTime, config);

					DecisionResponse stopLossShortCondition = engineClient.shouldBotPerformShortStopLossAction(itemInfo,
							correctionData, bot, openPositionsForItem, serverTime);

					if (stopLossShortCondition.isShouldBotActOnItem()) {
						// this item needs to be stop-lossed
						result = stopLossShortCondition;
					} else {
						// perform extension action
						DecisionResponse extendShortCondition = engineClient.shouldBotExtendShortPosition(itemInfo,
								correctionData, bot, openPositionsForItem, serverTime);
						if (extendShortCondition.isShouldBotActOnItem()) {
							// this item needs to be stop-lossed
							result = extendShortCondition;
						}
						extendShortCondition = null;
					}
					stopLossShortCondition = null;
					correctionData = null;
				}

			}

		}

		// initialize empty response if both entry possibilities fail
		if (result == null) {
			result = new DecisionResponse();
			result.setShouldBotActOnItem(false);
		}

		// LOG.info("Checked " + itemInfo);

		openPositionsForItem = null;
		marketClient = null;
		dataWrapper = null;
		serverTime = null;

		return result;
	}

	@Override
	public Map<String, List<TradeVO>> enterLongPositionAndPostExitTrade(List<TradeVO> openPositions, BotDefinition bot,
			ItemInfo itemInfo, Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// open a long position via market buy and post a limit sell trade to exit an
		// opened position.
		Map<String, List<TradeVO>> result = null;
		TradeVO entryResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		long currentTime = marketClient.getServerTime(bot);
		Map<String, List<Kline>> marketData = dataFactory.getDataWrapperForLifeCycle(bot)
				.getLongEntryMarketData(itemInfo, bot, currentTime, config);

		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}
		BigDecimal valueAtRisk = bot.getCurrentMoney()
				.multiply(new BigDecimal(config.get(SystemConstants.INITIAL_TRADE_MARGIN_KEY).toString()));
		BigDecimal quantity = valueAtRisk.divide(currentPrice, itemInfo.getQuantityPrecision(), RoundingMode.FLOOR);

		// if after rounding, the quantity is non zero, act.
		if (quantity.compareTo(BigDecimal.ZERO) > 0) {
			// non zero quantity post rounding. perform entry action.
			entryResult = marketClient.postTrade(currentPrice, quantity, SystemConstants.BUY_SIDE,
					SystemConstants.MARKET_ORDER, itemInfo, bot);
			entryResult.setPositionType(SystemConstants.LONG_POSITION);

			List<TradeVO> entryPosition = new ArrayList<TradeVO>();
			entryPosition.add(entryResult);

			BigDecimal exitPrice = engineClient.getExitSellPrice(itemInfo, marketData, bot, entryPosition, currentTime);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, quantity, SystemConstants.SELL_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);

			entryPosition.add(exitTrade);
			entryPosition.forEach(item -> {
				item.setBotName(bot.getBotName());
				item.setMarketName(bot.getMarketName());
				item.setLifeCycleName(bot.getLifeCycleName());
				item.setStrategyName(bot.getStrategyName());
				item.setPositionType(SystemConstants.LONG_POSITION);
			});

			result = new ConcurrentHashMap<String, List<TradeVO>>();
			result.put(SystemConstants.ADD_TRADES + itemInfo.toString(), entryPosition);

			entryPosition = null;
			exitPrice = null;
			exitTrade = null;
		} else {
			// zero quantity being bought post rounding. Cannot perform operation for this
			// item. Throw an exception for this item.
			throw new ZeroQuantityOrderedException();
		}

		quantity = null;
		currentPrice = null;
		valueAtRisk = null;

		return result;
	}

	@Override
	public Map<String, List<TradeVO>> enterShortPositionAndPostExitTrade(List<TradeVO> openPositions, BotDefinition bot, ItemInfo itemInfo,
			Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// open a short position via market sell and post limit buy to exit the
		// position.
		Map<String, List<TradeVO>> result = null;
		TradeVO entryResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		long currentTime = marketClient.getServerTime(bot);
		Map<String, List<Kline>> marketData = dataFactory.getDataWrapperForLifeCycle(bot)
				.getShortEntryMarketData(itemInfo, bot, currentTime, config);

		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}
		BigDecimal valueAtRisk = bot.getCurrentMoney()
				.multiply(new BigDecimal(config.get(SystemConstants.INITIAL_TRADE_MARGIN_KEY).toString()));
		BigDecimal quantity = valueAtRisk.divide(currentPrice);
		if (quantity.compareTo(BigDecimal.ZERO) > 0) {
			// non zero quantity post rounding. perform entry action.
			entryResult = marketClient.postTrade(currentPrice, quantity, SystemConstants.SELL_SIDE,
					SystemConstants.MARKET_ORDER, itemInfo, bot);

			List<TradeVO> entryPosition = new ArrayList<TradeVO>();
			entryPosition.add(entryResult);

			BigDecimal exitPrice = engineClient.getExitBuyPrice(itemInfo, marketData, bot, entryPosition, currentTime);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, quantity, SystemConstants.BUY_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);

			entryPosition.add(exitTrade);
			entryPosition.forEach(item -> {
				item.setBotName(bot.getBotName());
				item.setMarketName(bot.getMarketName());
				item.setLifeCycleName(bot.getLifeCycleName());
				item.setStrategyName(bot.getStrategyName());
				item.setPositionType(SystemConstants.SHORT_POSITION);
			});

			result = new ConcurrentHashMap<String, List<TradeVO>>();
			result.put(SystemConstants.ADD_TRADES + itemInfo.toString(), entryPosition);

			entryPosition = null;
			exitPrice = null;
			exitTrade = null;
		} else {
			// zero quantity being bought post rounding. Cannot perform operation for this
			// item. Throw an exception for this item.
			throw new ZeroQuantityOrderedException();
		}

		quantity = null;
		currentPrice = null;
		valueAtRisk = null;

		return result;
	}

	@Override
	public Map<String, List<TradeVO>> extendLongPosition(ItemInfo itemInfo, List<TradeVO> openPositions, BotDefinition bot,
			Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// extends an open long position via market buy and post a limit sell trade to
		// exit the cumulative position.
		Map<String, List<TradeVO>> result = null;
		TradeVO extensionResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		long currentTime = marketClient.getServerTime(bot);
		Map<String, List<Kline>> marketData = dataFactory.getDataWrapperForLifeCycle(bot)
				.getLongCorrectionMarketData(itemInfo, bot, currentTime, config);

		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}

		BigDecimal valueAtRisk = bot.getCurrentMoney()
				.multiply(new BigDecimal(config.get(SystemConstants.EXTENSION_TRADE_MARGIN_KEY).toString()));
		BigDecimal quantity = valueAtRisk.divide(currentPrice);

		if (quantity.compareTo(BigDecimal.ZERO) > 0) {
			// non zero quantity post rounding. perform entry action.
			result = new ConcurrentHashMap<String, List<TradeVO>>();
			result.put(SystemConstants.ADD_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
			result.put(SystemConstants.DELETE_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
			
			List<TradeVO> openPositionsForItem = new ArrayList<TradeVO>();
			openPositionsForItem.addAll(openPositions);
			openPositionsForItem.removeIf(item -> !(item.getTradeItem().equalsIgnoreCase(itemInfo.getSymbol())));
			
			extensionResult = marketClient.postTrade(currentPrice, quantity, SystemConstants.BUY_SIDE,
					SystemConstants.MARKET_ORDER, itemInfo, bot);
			
			openPositionsForItem.add(extensionResult);
			result.get(SystemConstants.ADD_TRADES + itemInfo.toString()).add(extensionResult);
			
			TradeVO openSellOrder = null;
			
			for (TradeVO trade : openPositionsForItem) {
				if (trade.getTradeTime() == null) {
					openSellOrder = trade;
					break;
				}
			}
			
			marketClient.cancelTrade(bot, openSellOrder, currentTime);
			result.get(SystemConstants.DELETE_TRADES + itemInfo.toString()).add(openSellOrder);
			
			String cancelledOrderId = openSellOrder.getTradeClientOrderId();
			
			openPositionsForItem.removeIf(item -> item.getTradeClientOrderId().equalsIgnoreCase(cancelledOrderId));

			BigDecimal exitPrice = engineClient.getExitSellPrice(itemInfo, marketData, bot, openPositionsForItem, currentTime);
			BigDecimal exitQuantity = this.getTotalQuantityInPosition(openPositions);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, exitQuantity, SystemConstants.SELL_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);
			exitTrade.setPositionType(SystemConstants.LONG_POSITION);

			openPositions.add(exitTrade);
			result.get(SystemConstants.ADD_TRADES + itemInfo.toString()).add(exitTrade);
			
			exitPrice = null;
			exitTrade = null;
			exitQuantity = null;
		} else {
			// zero quantity being bought post rounding. Cannot perform operation for this
			// item. Throw an exception.
			LOG.error("The bot attempted to perform an operation with zero quantity. Throwing exception here.");
			throw new ZeroQuantityOrderedException();
		}

		quantity = null;
		currentPrice = null;

		return result;
	}

	@Override
	public Map<String, List<TradeVO>> extendShortPosition(ItemInfo itemInfo, List<TradeVO> openPositions, BotDefinition bot,
			Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// extends an open long position via market buy and post a limit sell trade to
		// exit the cumulative position.
		Map<String, List<TradeVO>> result = null;
		TradeVO extensionResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		long currentTime = marketClient.getServerTime(bot);
		Map<String, List<Kline>> marketData = dataFactory.getDataWrapperForLifeCycle(bot)
				.getShortCorrectionMarketData(itemInfo, bot, currentTime, config);

		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}

		BigDecimal valueAtRisk = bot.getCurrentMoney()
				.multiply(new BigDecimal(config.get(SystemConstants.EXTENSION_TRADE_MARGIN_KEY).toString()));
		BigDecimal quantity = valueAtRisk.divide(currentPrice);
		quantity = quantity.setScale(itemInfo.getQuantityPrecision(), RoundingMode.FLOOR);

		if (quantity.compareTo(BigDecimal.ZERO) > 0) {
			// non zero quantity post rounding. perform entry action.
			result = new ConcurrentHashMap<String, List<TradeVO>>();
			result.put(SystemConstants.ADD_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
			result.put(SystemConstants.DELETE_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
			
			extensionResult = marketClient.postTrade(currentPrice, quantity, SystemConstants.SELL_SIDE,
					SystemConstants.MARKET_ORDER, itemInfo, bot);
			TradeVO openBuyOrder = openPositions.get(openPositions.size() - 1);
			openPositions.add(extensionResult);
			result.get(SystemConstants.ADD_TRADES + itemInfo.toString()).add(extensionResult);
			
			marketClient.cancelTrade(bot, openBuyOrder, currentTime);
			result.get(SystemConstants.DELETE_TRADES + itemInfo.toString()).add(openBuyOrder);
			
			BigDecimal exitPrice = engineClient.getExitBuyPrice(itemInfo, marketData, bot, openPositions, currentTime);
			exitPrice = exitPrice.setScale(itemInfo.getPricePrecision(), RoundingMode.CEILING);
			BigDecimal exitQuantity = this.getTotalQuantityInPosition(openPositions);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, exitQuantity, SystemConstants.BUY_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);
			exitTrade.setPositionType(SystemConstants.SHORT_POSITION);
			result.get(SystemConstants.ADD_TRADES + itemInfo.toString()).add(exitTrade);
			
			openPositions.add(exitTrade);
			openPositions = null;
			exitPrice = null;
			exitTrade = null;
		} else {
			// zero quantity being bought post rounding. Cannot perform operation for this
			// item. Throw an exception.
			LOG.error("The bot attempted to perform an operation with zero quantity. Throwing exception here.");
			throw new ZeroQuantityOrderedException();
		}

		quantity = null;
		currentPrice = null;

		return result;
	}

	@Override
	public Map<String, List<TradeVO>> stopLossLongPosition(ItemInfo itemInfo, List<TradeVO> currentPosition, BotDefinition bot,
			Map<String, Object> config) throws MarketDoesNotExistException, DataException {
		// stop-losses an open long position through a market sell order.
		Map<String, List<TradeVO>> result = null;
		TradeVO stopLossResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		long currentTime = marketClient.getServerTime(bot);
		Map<String, List<Kline>> marketData = dataFactory.getDataWrapperForLifeCycle(bot)
				.getLongCorrectionMarketData(itemInfo, bot, currentTime, config);

		result = new ConcurrentHashMap<String, List<TradeVO>>();
		result.put(SystemConstants.ADD_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
		result.put(SystemConstants.DELETE_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
		
		TradeVO openSellOrder = currentPosition.get(currentPosition.size() - 1);
		marketClient.cancelTrade(bot, openSellOrder, currentTime);
		result.get(SystemConstants.DELETE_TRADES + itemInfo.toString()).add(openSellOrder);
		
		BigDecimal totalQuantityInPosition = this.getTotalQuantityInPosition(currentPosition);
		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}

		stopLossResult = marketClient.postTrade(currentPrice, totalQuantityInPosition, SystemConstants.SELL_SIDE,
				SystemConstants.MARKET_ORDER, itemInfo, bot);
		result.get(SystemConstants.ADD_TRADES + itemInfo.toString()).add(stopLossResult);

		totalQuantityInPosition = null;
		currentPrice = null;
		openSellOrder = null;

		return result;
	}

	@Override
	public Map<String, List<TradeVO>> stopLossShortPosition(ItemInfo itemInfo, List<TradeVO> currentPosition, BotDefinition bot,
			Map<String, Object> config) throws MarketDoesNotExistException, DataException {
		// stop-losses an open short position through a market buy order.
		Map<String, List<TradeVO>> result = null;
		TradeVO stopLossResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		long currentTime = marketClient.getServerTime(bot);
		Map<String, List<Kline>> marketData = dataFactory.getDataWrapperForLifeCycle(bot)
				.getShortCorrectionMarketData(itemInfo, bot, currentTime, config);

		TradeVO openBuyOrder = currentPosition.get(currentPosition.size() - 1);
		marketClient.cancelTrade(bot, openBuyOrder, currentTime);
		
		result = new ConcurrentHashMap<String, List<TradeVO>>();
		result.put(SystemConstants.ADD_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
		result.put(SystemConstants.DELETE_TRADES + itemInfo.toString(), new ArrayList<TradeVO>());
		result.get(SystemConstants.DELETE_TRADES + itemInfo.toString()).add(openBuyOrder);
		
		BigDecimal totalQuantityInPosition = this.getTotalQuantityInPosition(currentPosition);
		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}

		stopLossResult = marketClient.postTrade(currentPrice, totalQuantityInPosition, SystemConstants.BUY_SIDE,
				SystemConstants.MARKET_ORDER, itemInfo, bot);
		result.get(SystemConstants.ADD_TRADES + itemInfo.toString()).add(stopLossResult);
		
		totalQuantityInPosition = null;
		currentPrice = null;
		openBuyOrder = null;

		return result;
	}

	@Override
	public Boolean hasPositionBeenClosed(List<TradeVO> openPositions, ItemInfo itemInfo, BotDefinition bot) throws MarketDoesNotExistException, DataException {
		MarketInterface marketClient = marketFactory.getClient(bot);
		
		List<TradeVO> openPositionsForItem = new ArrayList<TradeVO>();
		openPositionsForItem.addAll(openPositions);
		openPositionsForItem.removeIf(item -> itemInfo.getSymbol().equalsIgnoreCase(item.getTradeItem()));
		
		// this will yield only one position which is the cumulative exit trade
		openPositionsForItem.removeIf(item -> item.getTradeTime() != null);
		
		Boolean result = marketClient.isTradeFilled(bot, openPositionsForItem.get(0), marketClient.getServerTime(bot));
		
		return result;
	}

}
