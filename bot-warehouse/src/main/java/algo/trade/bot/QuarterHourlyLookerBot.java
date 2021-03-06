package algo.trade.bot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import algo.trade.bot.beans.TradeVO;
import algo.trade.constants.SystemConstants;
import algo.trade.decision.beans.DecisionResponse;
import algo.trade.decision.beans.EntryDecisionQuery;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.lifecycle.client.LifeCycle;
import algo.trade.market.beans.ItemInfo;
import algo.trade.market.beans.Kline;
import algo.trade.market.client.MarketInterface;

public class QuarterHourlyLookerBot extends Bot {

	private static final String NAME = "QHL";

	@Override
	public Boolean setupBot() {
		if (botConfigurationConstants == null) {
			// initialize bot config from decision engine
			EntryDecisionQuery request = new EntryDecisionQuery();
			request.setBot(botDefinition);
			botConfigurationConstants = engine.getBotConfigurationConstants(request);
		}

		MarketInterface marketClient;
		try {
			marketClient = marketFactory.getClient(botDefinition);
			if (botMarket == null) {
				// uncreated bot market
				botMarket = marketClient.getAllItemsData(botDefinition);
				filterBotMarketBasedOnAvailableHistory();
			}

			// initialize positions for the bot

		} catch (MarketDoesNotExistException e) {
			LOG.error("The market specified in the definition does not exist.");
		}

		return true;
	}

	private void filterBotMarketBasedOnAvailableHistory() throws MarketDoesNotExistException {
		// filter total bot market for initial config
		LOG.info("Attempting to initialize the market for the bot " + botDefinition);
		MarketInterface marketClient = marketFactory.getClient(botDefinition);
		long currentTime = marketClient.getServerTime(botDefinition);
		List<String> invalidItems = new ArrayList<String>();
		for (String item : botMarket.keySet()) {
			List<Kline> itemHistory = marketClient.getHistoricKlines(botMarket.get(item).getSymbol(),
					SystemConstants.ONE_DAY, Integer.valueOf(botConfigurationConstants
							.get(SystemConstants.MINIMUM_ITEM_TRADE_HISTORY_DAYS_KEY).toString()),
					currentTime);

			if (itemHistory.size() < Integer.valueOf(
					botConfigurationConstants.get(SystemConstants.MINIMUM_ITEM_TRADE_HISTORY_DAYS_KEY).toString())) {
				invalidItems.add(item);
			}
			itemHistory = null;
		}

		LOG.info("Initial market for bot " + botMarket.size());

		invalidItems.forEach(item -> botMarket.remove(item));

		LOG.info("Initialized market for bot with " + botMarket.size() + " after removing " + invalidItems.size()
				+ " items.");

		marketClient = null;
		invalidItems = null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void startBot() {
		// Does the actions specified
		Calendar rightNow = Calendar.getInstance();
		if (rightNow.get(Calendar.MINUTE) % 15 == 0) {
			// 15 mins period, monitor the market.
			try {
				LifeCycle lifeCycleForBot = lifecycleFactory.getLifecycle(botDefinition.getLifeCycleName());
				List<DecisionResponse> itemResults = new ArrayList<DecisionResponse>();

				// monitoring operation for full market
				for (String item : botMarket.keySet()) {
					DecisionResponse itemResult = lifeCycleForBot.performMonitorOperationForItem(botMarket.get(item),
							botDefinition, outstandingBotPositions, botConfigurationConstants);
					itemResults.add(itemResult);
					itemResult = null;
				}

				LOG.info("Performed checks for " + itemResults.size() + " items.");

				// remove negative results
				itemResults.removeIf(item -> !item.isShouldBotActOnItem());

				if (itemResults.size() != 0) {
					// some actions can be taken.
					LOG.info("Successful results found for " + itemResults.size() + " items.");
					Map<String, List<TradeVO>> tradeSets = new ConcurrentHashMap<String, List<TradeVO>>();
					for (TradeVO trade : outstandingBotPositions) {
						if (!tradeSets.containsKey(trade.getTradeItem())) {
							tradeSets.put(trade.getTradeItem(), new ArrayList<TradeVO>());
						}
						tradeSets.get(trade.getTradeItem()).add(trade);
					}

					int availableSpots = Integer
							.valueOf(botConfigurationConstants.get(SystemConstants.MAX_PORTFOLIO_SIZE_KEY).toString())
							- tradeSets.size();

					// filter top n responses for action
					List<BigDecimal> changeMagnitude = new ArrayList<BigDecimal>();
					for (DecisionResponse itemResult : itemResults) {
						changeMagnitude
								.add(itemResult.getDecisionParameters().get(SystemConstants.COMPARISON_INDEX_KEY));
					}

					// get greatest change objects.
					Collections.sort(changeMagnitude);
					if (availableSpots >= changeMagnitude.size()) {
						// more available spots than actions available, take all actions.
					} else {
						// filter top decisions only.
						changeMagnitude = changeMagnitude.subList(changeMagnitude.size() - availableSpots - 1,
								changeMagnitude.size() - 1);
					}
					Map<String, List<TradeVO>> allUpdatedPositions = new ConcurrentHashMap<String, List<TradeVO>>();

					for (DecisionResponse itemResult : itemResults) {
						String actionToTake = itemResult.getConfigParameters()
								.get(SystemConstants.DECISION_TAKING_ACTION_KEY).toString();
						Map<String, List<TradeVO>> updatedPositions = null;
						
						if (SystemConstants.LONG_ENTRY_ACTION.equalsIgnoreCase(actionToTake)) {
							ItemInfo itemInfo = objectMapper.convertValue(
									itemResult.getConfigParameters().get(SystemConstants.ITEM_INFO_KEY),
									ItemInfo.class);
							updatedPositions = lifeCycleForBot.enterLongPositionAndPostExitTrade(tradeSets.get(itemInfo.getSymbol()), botDefinition,
									itemInfo, botConfigurationConstants);
							
						} else if (SystemConstants.LONG_EXTEND_ACTION.equalsIgnoreCase(actionToTake)) {

							ItemInfo itemInfo = objectMapper.convertValue(
									itemResult.getConfigParameters().get(SystemConstants.ITEM_INFO_KEY),
									ItemInfo.class);
							updatedPositions = lifeCycleForBot.extendLongPosition(itemInfo, tradeSets.get(itemInfo.getSymbol()),
									botDefinition, botConfigurationConstants);

						} else if (SystemConstants.LONG_STOP_LOSS_ACTION.equalsIgnoreCase(actionToTake)) {

							ItemInfo itemInfo = objectMapper.convertValue(
									itemResult.getConfigParameters().get(SystemConstants.ITEM_INFO_KEY),
									ItemInfo.class);
							updatedPositions = lifeCycleForBot.stopLossLongPosition(itemInfo, tradeSets.get(itemInfo.getSymbol()),
									botDefinition, botConfigurationConstants);

						} else if (SystemConstants.SHORT_ENTRY_ACTION.equalsIgnoreCase(actionToTake)) {
							ItemInfo itemInfo = objectMapper.convertValue(
									itemResult.getConfigParameters().get(SystemConstants.ITEM_INFO_KEY),
									ItemInfo.class);
							updatedPositions = lifeCycleForBot.enterShortPositionAndPostExitTrade(tradeSets.get(itemInfo.getSymbol()), botDefinition,
									itemInfo, botConfigurationConstants);

						} else if (SystemConstants.SHORT_EXTEND_ACTION.equalsIgnoreCase(actionToTake)) {

							ItemInfo itemInfo = objectMapper.convertValue(
									itemResult.getConfigParameters().get(SystemConstants.ITEM_INFO_KEY),
									ItemInfo.class);
							updatedPositions = lifeCycleForBot.extendShortPosition(itemInfo, tradeSets.get(itemInfo.getSymbol()),
									botDefinition, botConfigurationConstants);

						} else if (SystemConstants.SHORT_STOP_LOSS_ACTION.equalsIgnoreCase(actionToTake)) {

							ItemInfo itemInfo = objectMapper.convertValue(
									itemResult.getConfigParameters().get(SystemConstants.ITEM_INFO_KEY),
									ItemInfo.class);
							updatedPositions = lifeCycleForBot.stopLossShortPosition(itemInfo, tradeSets.get(itemInfo.getSymbol()),
									botDefinition, botConfigurationConstants);

						} else if (SystemConstants.POSITION_CLOSE_ACTION.equalsIgnoreCase(actionToTake)) {
							// position close action. log changes to external storage.
							updatedPositions = objectMapper.convertValue(
									itemResult.getConfigParameters().get(SystemConstants.UPDATED_POSITIONS_KEY),
									Map.class);
						}
						allUpdatedPositions.putAll(updatedPositions);
						updatedPositions = null;
					}
					
					logPositionChanges(allUpdatedPositions);

					changeMagnitude = null;
					tradeSets = null;
				} else {
					// no actions can be taken. Skip future executions.

				}
			} catch (Exception e) {
				// TODO: handle exception
				LOG.error("There was an error.");
				e.printStackTrace();
			}
		} else {
			// do nothing.
			LOG.info("This is not the time to do something.");
		}
	}

	@Override
	public void stopBot() {
		// TODO Auto-generated method stub
		// stop api call redirects here to stop bot in operations
	}

	@Override
	public String getBotName() {
		return NAME;
	}

	@Override
	public void logPositionChanges(Map<String, List<TradeVO>> updatedPositions) {
		// logs position changes to external storage.
	}

}
