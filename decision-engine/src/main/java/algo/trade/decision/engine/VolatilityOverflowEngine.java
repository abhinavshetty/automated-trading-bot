package algo.trade.decision.engine;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.springframework.stereotype.Service;

import algo.trade.bot.beans.TradeVO;
import algo.trade.constants.SystemConstants;
import algo.trade.decision.beans.BollingerBandValues;
import algo.trade.decision.beans.DecisionResponse;
import algo.trade.decision.beans.EntryDecisionQuery;
import algo.trade.decision.beans.SecondaryActionDecisionQuery;
import algo.trade.decision.client.DecisionEngine;
import algo.trade.errors.PositionOpenException;
import algo.trade.market.beans.Kline;

/**
 * Checks for deviations beyond the bollinger bands
 * 
 * @author Abhinav Shetty
 *
 */
@Service
public class VolatilityOverflowEngine extends DecisionEngine {

	private static final String[] STRATEGIES = { "VOE" };

	@Override
	public String[] getStrategies() {
		return VolatilityOverflowEngine.STRATEGIES;
	}

	@Override
	public DecisionResponse shouldBotOpenLongPosition(EntryDecisionQuery request) {
		DecisionResponse result = constructEmptyResponse();

		List<Kline> quarterHourlyData = request.getMarketData()
				.get(botConfigurationConstants.get("ENTRY_MONITORING_KLINE_1"));
		List<Kline> hourlyData = request.getMarketData()
				.get(botConfigurationConstants.get("ENTRY_MONITORING_KLINE_2"));

		BollingerBandValues hourlyBands = this.calculateBollingerBandsForInput(hourlyData);
		BollingerBandValues quarterHourlyBands = this.calculateBollingerBandsForInput(quarterHourlyData);

		BigDecimal currentPrice = quarterHourlyData.get(quarterHourlyData.size() - 1).getClose();
		BigDecimal lastHourLowerBB = hourlyBands.getLowerBand()[hourlyBands.getLowerBand().length - 1];
		BigDecimal lastHourMiddleBB = hourlyBands.getMiddleBand()[hourlyBands.getMiddleBand().length - 1];
		BigDecimal lastQuarterHourLowerBB = quarterHourlyBands.getLowerBand()[quarterHourlyBands.getLowerBand().length
				- 1];

		int localRsi = getRsi(hourlyData);

		// hyperlocal volatility excess
		Boolean intermediateResult = currentPrice.compareTo(lastQuarterHourLowerBB) < 0;

		// hourly volatility measure
		intermediateResult = intermediateResult && currentPrice.compareTo(lastHourLowerBB) > 0;
		intermediateResult = intermediateResult && currentPrice.compareTo(lastHourMiddleBB) < 0;

		if ((localRsi <= ((Integer) engineConfigurationConstants.get("LOWER_RSI_THRESHOLD"))) && intermediateResult
				&& currentPrice.compareTo(
						quarterHourlyBands.getLowerBand()[quarterHourlyBands.getLowerBand().length - 1]) < 0) {
			result.setShouldBotActOnItem(true);

		}
		return result;
	}

	private int getRsi(List<Kline> klines) {
		BigDecimal rsi = BigDecimal.ZERO;
		int firstIndexToCheck = klines.size() - 2;
		for (int i = 0; i < Integer
				.valueOf(engineConfigurationConstants.get("BOLLINGER_BAND_MA_INTERVAL").toString()); i++) {
			rsi = ((klines.get(firstIndexToCheck - i).getClose())
					.compareTo(klines.get(firstIndexToCheck - i).getOpen()) > 0) ? rsi.add(BigDecimal.ONE)
							: rsi.add(BigDecimal.ZERO);
		}

		rsi = (rsi.divide(new BigDecimal(engineConfigurationConstants.get("BOLLINGER_BAND_MA_INTERVAL").toString())))
				.multiply(new BigDecimal(100));

		return rsi.intValue();
	}

	private BollingerBandValues calculateBollingerBandsForInput(final List<Kline> marketData) {
		BollingerBandValues result = new BollingerBandValues();

		int counter = 0;

		for (; counter < Integer.valueOf(engineConfigurationConstants.get("ENTRY_MONITORING_WINDOW").toString())
				- Integer.valueOf(
						engineConfigurationConstants.get("BOLLINGER_BAND_MA_INTERVAL").toString()); counter++) {
			List<Kline> marketDataWindow = marketData.subList(counter, counter
					+ Integer.valueOf(getEngineConfigurationConstants().get("BOLLINGER_BAND_MA_INTERVAL").toString()));
			BigDecimal mean = BigDecimal.ZERO;
			BigDecimal stdDev = BigDecimal.ZERO;
			for (Kline item : marketDataWindow) {
				mean = mean.add(item.getClose());
			}
			mean = mean
					.divide(new BigDecimal(engineConfigurationConstants.get("BOLLINGER_BAND_MA_INTERVAL").toString()));

			for (Kline item : marketDataWindow) {
				BigDecimal difference = item.getClose();

				difference = difference.subtract(mean);
				difference = difference.multiply(difference);
				stdDev = stdDev.add(difference);
			}

			stdDev = stdDev
					.divide(new BigDecimal(engineConfigurationConstants.get("BOLLINGER_BAND_MA_INTERVAL").toString()));
			stdDev = new BigDecimal(Math.sqrt(stdDev.doubleValue()), MathContext.DECIMAL32);

			result.getMiddleBand()[counter] = mean;
			result.getLowerBand()[counter] = mean.subtract((stdDev.multiply(new BigDecimal(2))));
			result.getUpperBand()[counter] = mean.add((stdDev.multiply(new BigDecimal(2))));
		}

		return result;
	}

	@Override
	public DecisionResponse shouldBotExtendLongPosition(SecondaryActionDecisionQuery request) {
		DecisionResponse result = constructEmptyResponse();
		BigDecimal firstEntryPrice = request.getCurrentOutstandingTrades().get(0).getBuyPrice();
		BigDecimal currentPrice = request.getMarketData()
				.get(engineConfigurationConstants.get("EXTENSION_MONITORING_KLINE"))
				.get(request.getMarketData().get("EXTENSION_MONITORING_KLINE").size() - 1).getClose();

		result.setShouldBotActOnItem((firstEntryPrice.multiply(
				(BigDecimal.ONE.subtract((BigDecimal) engineConfigurationConstants.get("LONG_EXTENSION_THRESHOLD")))))
						.compareTo(currentPrice) > 0);

		return result;
	}

	@Override
	public DecisionResponse shouldBotPerformLongStopLossAction(SecondaryActionDecisionQuery request) {
		DecisionResponse result = constructEmptyResponse();
		try {
			BigDecimal currentPrice = request.getMarketData()
					.get(engineConfigurationConstants.get("EXTENSION_MONITORING_KLINE")).get(request.getMarketData()
							.get(engineConfigurationConstants.get("EXTENSION_MONITORING_KLINE")).size() - 1)
					.getClose();

			List<TradeVO> currentPosition = request.getCurrentOutstandingTrades();
			currentPosition.forEach(item -> item.setSellPrice(currentPrice));

			BigDecimal unrealizedLoss = this.getTotalReturnFromTradesInList(currentPosition);

			result.setShouldBotActOnItem(unrealizedLoss.compareTo(request.getBot().getCurrentMoney()
					.multiply((BigDecimal) engineConfigurationConstants.get("LONG_STOP_LOSS_THRESHOLD"))) < 0);
		} catch (PositionOpenException e) {
			LOG.error("One or more positions do not have buy prices defined. Please check the request for item "
					+ request.getItemInfo());
			result.setShouldBotActOnItem(false);
		}
		return result;
	}

	@Override
	public DecisionResponse shouldBotOpenShortPosition(EntryDecisionQuery request) {
		DecisionResponse result = constructEmptyResponse();

		// List<Kline> hourlyData =
		// request.getMarketData().get(getBotConfigurationConstants().get("ENTRY_MONITORING_WINDOW").toString());
		// BollingerBandValues bands = this.calculateBollingerBandsForInput(hourlyData);
		//
		// int rsi = getRsi(hourlyData);
		// Kline currentPrice = hourlyData.get(hourlyData.size() - 1);
		// Kline dayAgoPrice = hourlyData.get(hourlyData.size() - 25);
		// boolean thresholdPriceChange = currentPrice.getClose() >
		// dayAgoPrice.getClose() * 1.15;
		//
		// if (rsi >= UPPER_RSI_THRESHOLD && currentPrice.getOpen() > (new BigDecimal(1
		// + (3 * 0.01)))
		// .multiply(bands.getUpperBand()[bands.getUpperBand().length -
		// 1]).doubleValue()) {
		// result = thresholdPriceChange;
		// }
		return result;
	}

	@Override
	public DecisionResponse shouldBotExtendShortPosition(SecondaryActionDecisionQuery request) {
		// never sell more for this trade
		DecisionResponse result = constructEmptyResponse();
		return result;
	}

	@Override
	public DecisionResponse shouldBotPerformShortStopLossAction(SecondaryActionDecisionQuery request) {
		// no need for short stop loss because this position is never opened
		DecisionResponse result = constructEmptyResponse();
		return result;
	}

	@Override
	public BigDecimal getExitSellPrice(SecondaryActionDecisionQuery request) {
		// calculate the exit price as per input

		BigDecimal result = BigDecimal.ZERO;
		BigDecimal entryPrice;
		try {
			entryPrice = this.getWeightedAverageSidePriceForPosition(request.getCurrentOutstandingTrades(),
					SystemConstants.BUY_SIDE);
			result = entryPrice
					.multiply(BigDecimal.ONE.add((BigDecimal) engineConfigurationConstants.get("LONG_PROFIT")));
		} catch (PositionOpenException e) {
			LOG.error("One or more buy prices could not be read from the request. Please check the request for item : "
					+ request.getItemInfo());
		}

		return result;
	}

	@Override
	public BigDecimal getExitBuyPrice(SecondaryActionDecisionQuery request) {
		// calculate the exit price as per input

		BigDecimal result = BigDecimal.ZERO;
		BigDecimal entryPrice;
		try {
			entryPrice = this.getWeightedAverageSidePriceForPosition(request.getCurrentOutstandingTrades(),
					SystemConstants.SELL_SIDE);
			result = entryPrice
					.multiply(BigDecimal.ONE.subtract((BigDecimal) engineConfigurationConstants.get("SHORT_PROFIT")));
		} catch (PositionOpenException e) {
			LOG.error("One or more sell prices could not be read from the request. Please check the request for item : "
					+ request.getItemInfo());
		}

		return result;
	}

	@Override
	protected void initializeBotConfigurationConstants() {
		botConfigurationConstants.put(SystemConstants.INITIAL_TRADE_MARGIN_KEY, 0.05d);
		botConfigurationConstants.put(SystemConstants.EXTENSION_TRADE_MARGIN_KEY, 0.1d);
		botConfigurationConstants.put(SystemConstants.MINIMUM_ITEM_TRADE_HISTORY_DAYS_KEY, 60);
		
		botConfigurationConstants.put(SystemConstants.ENTRY_MONITORING_KLINE + "_1", "15m");
		botConfigurationConstants.put(SystemConstants.ENTRY_MONITORING_KLINE + "_2", "1h");
		botConfigurationConstants.put(SystemConstants.ENTRY_MONITORING_WINDOW_HOURS_KEY, 100);

		botConfigurationConstants.put(SystemConstants.EXTENSION_MONITORING_KLINE_KEY, "15m");
		botConfigurationConstants.put(SystemConstants.EXTENSION_MONITORING_WINDOW_HOURS_KEY, 10);
	}

	@Override
	protected void initializeEngineConfigurationConstants() {
		engineConfigurationConstants.put("LONG_PROFIT", new BigDecimal(0.15d));
		engineConfigurationConstants.put("LONG_STOP_LOSS_THRESHOLD", new BigDecimal(-0.05d));
		engineConfigurationConstants.put("LONG_STOP_LOSS_THRESHOLD", new BigDecimal(-0.05d));
		engineConfigurationConstants.put("UPPER_RSI_THRESHOLD", 65);
		engineConfigurationConstants.put("LOWER_RSI_THRESHOLD", 35);
		engineConfigurationConstants.put("BOLLINGER_BAND_MA_INTERVAL", 20);

	}

	@Override
	public BigDecimal getStopLossSellPrice(SecondaryActionDecisionQuery request) {
		// triggers market sell on triggered stop loss
		BigDecimal result = BigDecimal.ZERO;
		return result;
	}

	@Override
	public BigDecimal getStopLossBuyPrice(SecondaryActionDecisionQuery request) {
		// triggers market buy on triggered stop loss
		BigDecimal result = BigDecimal.ZERO;
		return result;
	}

}
