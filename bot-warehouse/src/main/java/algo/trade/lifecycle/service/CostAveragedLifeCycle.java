package algo.trade.lifecycle.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			List<TradeVO> openPositions, Map<String, Object> config)
			throws MarketDoesNotExistException, DataException, PositionOpenException {
		// monitors an item for decision engine
		DecisionResponse result = null;
		List<TradeVO> openPositionsForItem = openPositions;
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
		} else {
			// there are open positions for this item. Evaluate stop-loss or extension
			// possibilities
			if (openPositionsForItem.get(0).getTradeSide() == SystemConstants.LONG_TRADE) {
				// open long position: check for close followed by stop loss followed by
				// extension

				if (marketClient.isTradeFilled(bot, openPositionsForItem.get(openPositionsForItem.size() - 1),
						serverTime)) {
					LOG.info("The long position in " + itemInfo.getSymbol() + " was closed successfully at a profit.");
					for (TradeVO trade : openPositionsForItem) {
						trade.setBuyTime(new Timestamp(serverTime));
					}
					LOG.info("Trade profit : " + this.getTotalReturnFromTradesInList(openPositionsForItem));
					openPositions.removeIf(item -> item.getTradeItem().equalsIgnoreCase(itemInfo.getSymbol()));
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
				if (marketClient.isTradeFilled(bot, openPositionsForItem.get(openPositionsForItem.size() - 1),
						serverTime)) {
					LOG.info("The long position in " + itemInfo.getSymbol() + " was closed successfully at a profit.");
					for (TradeVO trade : openPositionsForItem) {
						trade.setBuyTime(new Timestamp(serverTime));
					}
					LOG.info("Trade profit : " + this.getTotalReturnFromTradesInList(openPositionsForItem));
					openPositions.removeIf(item -> item.getTradeItem().equalsIgnoreCase(itemInfo.getSymbol()));
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

		LOG.info("Checked " + itemInfo);
		
		openPositionsForItem = null;
		marketClient = null;
		dataWrapper = null;
		serverTime = null;

		return result;
	}

	@Override
	public TradeVO enterLongPositionAndPostExitTrade(BotDefinition bot, ItemInfo itemInfo,
			Map<String, List<Kline>> marketData, long currentTime, Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// open a long position via market buy and post a limit sell trade to exit an
		// opened position.
		TradeVO entryResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}
		BigDecimal valueAtRisk = bot.getCurrentMoney()
				.multiply(new BigDecimal(config.get(SystemConstants.INITIAL_TRADE_MARGIN_KEY).toString()));
		BigDecimal quantity = valueAtRisk.divide(currentPrice);
		
		if (quantity.compareTo(BigDecimal.ZERO) > 0) {
			// non zero quantity post rounding. perform entry action.
			entryResult = marketClient.postTrade(currentPrice, quantity, SystemConstants.BUY_SIDE,
					SystemConstants.MARKET_ORDER, itemInfo, bot);
			entryResult.setTradeSide(SystemConstants.LONG_TRADE);

			List<TradeVO> entryPosition = new ArrayList<TradeVO>();
			entryPosition.add(entryResult);

			BigDecimal exitPrice = engineClient.getExitSellPrice(itemInfo, marketData, bot, entryPosition, currentTime);
			exitPrice = exitPrice.setScale(itemInfo.getPricePrecision(), RoundingMode.CEILING);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, quantity, SystemConstants.SELL_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);

			entryResult.setSellClientOrderId(exitTrade.getSellClientOrderId());
			entryResult.setSellOrderId(exitTrade.getSellOrderId());
			entryResult.setSellPrice(exitTrade.getSellPrice());

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

		return entryResult;
	}

	@Override
	public TradeVO enterShortPositionAndPostExitTrade(BotDefinition bot, ItemInfo itemInfo,
			Map<String, List<Kline>> marketData, long currentTime, Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// open a short position via market sell and post limit buy to exit the
		// position.
		TradeVO entryResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

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
			entryResult.setTradeSide(SystemConstants.SHORT_TRADE);

			List<TradeVO> entryPosition = new ArrayList<TradeVO>();
			entryPosition.add(entryResult);

			BigDecimal exitPrice = engineClient.getExitBuyPrice(itemInfo, marketData, bot, entryPosition, currentTime);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, quantity, SystemConstants.BUY_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);

			entryResult.setBuyClientOrderId(exitTrade.getBuyClientOrderId());
			entryResult.setBuyOrderId(exitTrade.getBuyOrderId());
			entryResult.setBuyPrice(exitTrade.getBuyPrice());

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

		return entryResult;
	}

	@Override
	public TradeVO extendLongPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long currentTime, BotDefinition bot, Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// extends an open long position via market buy and post a limit sell trade to
		// exit the cumulative position.
		TradeVO extensionResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}

		BigDecimal valueAtRisk = bot.getCurrentMoney()
				.multiply(new BigDecimal(config.get(SystemConstants.EXTENSION_TRADE_MARGIN_KEY).toString()));
		BigDecimal quantity = valueAtRisk.divide(currentPrice);

		if (quantity.compareTo(BigDecimal.ZERO) > 0) {
			// non zero quantity post rounding. perform entry action.
			extensionResult = marketClient.postTrade(currentPrice, quantity, SystemConstants.BUY_SIDE,
					SystemConstants.MARKET_ORDER, itemInfo, bot);
			extensionResult.setTradeSide(SystemConstants.LONG_TRADE);

			TradeVO openSellOrder = currentPosition.get(currentPosition.size() - 1);
			currentPosition.add(extensionResult);

			marketClient.cancelTrade(bot, openSellOrder, currentTime);

			BigDecimal exitPrice = engineClient.getExitSellPrice(itemInfo, marketData, bot, currentPosition,
					currentTime);
			BigDecimal exitQuantity = this.getTotalQuantityInPosition(currentPosition);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, exitQuantity, SystemConstants.SELL_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);

			extensionResult.setSellClientOrderId(exitTrade.getSellClientOrderId());
			extensionResult.setSellOrderId(exitTrade.getSellOrderId());
			extensionResult.setSellPrice(exitPrice);

			currentPosition = null;
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

		return extensionResult;
	}

	@Override
	public TradeVO extendShortPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long currentTime, BotDefinition bot, Map<String, Object> config)
			throws ZeroQuantityOrderedException, MarketDoesNotExistException, DataException {
		// extends an open long position via market buy and post a limit sell trade to
		// exit the cumulative position.
		TradeVO extensionResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

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
			extensionResult = marketClient.postTrade(currentPrice, quantity, SystemConstants.SELL_SIDE,
					SystemConstants.MARKET_ORDER, itemInfo, bot);
			TradeVO openBuyOrder = currentPosition.get(currentPosition.size() - 1);
			currentPosition.add(extensionResult);

			marketClient.cancelTrade(bot, openBuyOrder, currentTime);

			BigDecimal exitPrice = engineClient.getExitBuyPrice(itemInfo, marketData, bot, currentPosition,
					currentTime);
			exitPrice = exitPrice.setScale(itemInfo.getPricePrecision(), RoundingMode.CEILING);
			BigDecimal exitQuantity = this.getTotalQuantityInPosition(currentPosition);

			TradeVO exitTrade = marketClient.postTrade(exitPrice, exitQuantity, SystemConstants.BUY_SIDE,
					SystemConstants.LIMIT_ORDER, itemInfo, bot);

			extensionResult.setBuyClientOrderId(exitTrade.getBuyClientOrderId());
			extensionResult.setBuyOrderId(exitTrade.getBuyOrderId());
			extensionResult.setBuyPrice(exitPrice);

			currentPosition = null;
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

		return extensionResult;
	}

	@Override
	public TradeVO stopLossLongPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long currentTime, BotDefinition bot, Map<String, Object> config)
			throws MarketDoesNotExistException, DataException {
		// stop-losses an open long position through a market sell order.
		TradeVO stopLossResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		TradeVO openBuyOrder = currentPosition.get(currentPosition.size() - 1);
		marketClient.cancelTrade(bot, openBuyOrder, currentTime);

		BigDecimal totalQuantityInPosition = this.getTotalQuantityInPosition(currentPosition);
		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}

		stopLossResult = marketClient.postTrade(currentPrice, totalQuantityInPosition, SystemConstants.SELL_SIDE,
				SystemConstants.MARKET_ORDER, itemInfo, bot);

		totalQuantityInPosition = null;
		currentPrice = null;
		openBuyOrder = null;

		return stopLossResult;
	}

	@Override
	public TradeVO stopLossShortPosition(ItemInfo itemInfo, List<TradeVO> currentPosition,
			Map<String, List<Kline>> marketData, long currentTime, BotDefinition bot, Map<String, Object> config)
			throws MarketDoesNotExistException, DataException {
		// stop-losses an open short position through a market buy order.
		TradeVO stopLossResult = null;
		BigDecimal currentPrice = null;
		MarketInterface marketClient = marketFactory.getClient(bot);

		TradeVO openSellOrder = currentPosition.get(currentPosition.size() - 1);
		marketClient.cancelTrade(bot, openSellOrder, currentTime);

		BigDecimal totalQuantityInPosition = this.getTotalQuantityInPosition(currentPosition);
		for (String klineSet : marketData.keySet()) {
			currentPrice = marketData.get(klineSet).get(marketData.get(klineSet).size() - 1).getClose();
			break;
		}

		stopLossResult = marketClient.postTrade(currentPrice, totalQuantityInPosition, SystemConstants.BUY_SIDE,
				SystemConstants.MARKET_ORDER, itemInfo, bot);

		totalQuantityInPosition = null;
		currentPrice = null;
		openSellOrder = null;

		return stopLossResult;
	}

}
