package algo.trade.market.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.ExchangeInfoEntry;
import com.binance.client.model.trade.MyTrade;
import com.binance.client.model.trade.Order;
import com.binance.client.model.trade.PositionRisk;

import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.constants.SystemConstants;
import algo.trade.errors.DataException;
import algo.trade.market.beans.ItemInfo;
import algo.trade.market.beans.Kline;

/**
 * Service to interact with the futures market.
 * 
 * @author Abhinav Shetty
 */
@Service
public class BinanceFuturesMarketClient extends MarketInterface {

	private static final String[] MARKETS = { "BINANCE_FUTURES" };

	private final RequestOptions opt = new RequestOptions();
	private SyncRequestClient client;

	@Value("${binance.api.header.key}")
	private String headerKey;

	@Value("${binance.api.encrypt.key}")
	private String encryptKey;

	private SyncRequestClient getClient() {
		if (client == null) {
			client = SyncRequestClient.create(headerKey, encryptKey, opt);
		}
		return client;
	}

	private CandlestickInterval getCandlestickInterval(String input) {
		switch (input) {

		case SystemConstants.ONE_DAY:
			return CandlestickInterval.DAILY;

		case SystemConstants.TWELVE_HOURS:
			return CandlestickInterval.TWELVE_HOURLY;

		case SystemConstants.SIX_HOURS:
			return CandlestickInterval.SIX_HOURLY;

		case SystemConstants.FOUR_HOURS:
			return CandlestickInterval.FOUR_HOURLY;

		case SystemConstants.TWO_HOURS:
			return CandlestickInterval.TWO_HOURLY;

		case SystemConstants.ONE_HOUR:
			return CandlestickInterval.HOURLY;

		case SystemConstants.HALF_HOUR:
			return CandlestickInterval.HALF_HOURLY;

		case SystemConstants.QUARTER_HOUR:
			return CandlestickInterval.FIFTEEN_MINUTES;

		case SystemConstants.FIVE_MINUTES:
			return CandlestickInterval.FIVE_MINUTES;

		case SystemConstants.ONE_MINUTE:
			return CandlestickInterval.ONE_MINUTE;

		default:
			return CandlestickInterval.HOURLY;
		}
	}

	@Override
	public List<TradeVO> getAccountData(BotDefinition bot) {
		List<TradeVO> trades = new ArrayList<TradeVO>();
		List<PositionRisk> positions = getClient().getPositionRisk();

		positions.removeIf(item -> item.getPositionAmt().compareTo(BigDecimal.ZERO) == 0);

		for (PositionRisk position : positions) {
			List<Order> openOrders = getClient().getOpenOrders(position.getSymbol());

			if (position.getPositionAmt().compareTo(BigDecimal.ZERO) > 0) {
				// entry position trade
				TradeVO entryTrade = new TradeVO();
				entryTrade.setTradeItem(position.getSymbol());
				entryTrade.setTradePrice(position.getEntryPrice());
				entryTrade.setTradeTime(new Timestamp(System.currentTimeMillis()));
				entryTrade.setTradeClientOrderId(getRandomOrderId());
				entryTrade.setTradeOrderId(Long.valueOf(getRandomOrderId()));
				entryTrade.setQuantity(position.getPositionAmt());
				entryTrade.setPositionType(SystemConstants.LONG_POSITION);
				trades.add(entryTrade);
				
				// long position
				for (Order order : openOrders) {
					TradeVO trade = new TradeVO();
					trade.setTradeItem(position.getSymbol());
					trade.setTradePrice(position.getEntryPrice());
					trade.setTradeClientOrderId(getRandomOrderId());
					trade.setTradeOrderId(Long.valueOf(getRandomOrderId()));
					trade.setQuantity(order.getOrigQty());
					trades.add(trade);

					trade = null;
				}

			} else {
				// short position
				TradeVO entryTrade = new TradeVO();
				entryTrade.setTradeItem(position.getSymbol());
				entryTrade.setTradePrice(position.getEntryPrice());
				entryTrade.setTradeTime(new Timestamp(System.currentTimeMillis()));
				entryTrade.setTradeClientOrderId(getRandomOrderId());
				entryTrade.setTradeOrderId(Long.valueOf(getRandomOrderId()));
				entryTrade.setQuantity(position.getPositionAmt());
				entryTrade.setPositionType(SystemConstants.SHORT_POSITION);
				trades.add(entryTrade);
				
				for (Order order : openOrders) {
					TradeVO trade = new TradeVO();
					trade.setTradeItem(position.getSymbol());
					trade.setTradePrice(position.getEntryPrice());
					trade.setTradeTime(new Timestamp(System.currentTimeMillis()));
					trade.setTradeClientOrderId(getRandomOrderId());
					trade.setTradeOrderId(Long.valueOf(getRandomOrderId()));
					trade.setQuantity(order.getOrigQty());
					trades.add(trade);

					trade = null;
				}
			}
		}

		return trades;

	}

	@Override
	public List<String> getMarkets() {
		return Arrays.asList(MARKETS);
	}

	@Override
	public long getServerTime(BotDefinition bot) {
		List<Candlestick> rawData = getClient().getCandlestick("BTCUSDT", CandlestickInterval.ONE_MINUTE, null, null,
				1);
		long time = (long) ((rawData.get(0).getOpenTime() + rawData.get(0).getCloseTime()) / 2);

		return time;
	}

	@Override
	public Boolean cancelTrade(BotDefinition bot, TradeVO trade, long serverTime) {
		// cancel a pre-existing order
		getClient().cancelOrder(trade.getTradeItem(), trade.getTradeOrderId(), null);

		return true;
	}

	@Override
	public Map<String, ItemInfo> getAllItemsData(BotDefinition bot) {
		// Fetch all tickers.
		List<ExchangeInfoEntry> symbols = getClient().getExchangeInformation().getSymbols();
		Map<String, ItemInfo> result = new ConcurrentHashMap<String, ItemInfo>();
		for (ExchangeInfoEntry symbol : symbols) {
			if (symbol.getSymbol().endsWith("USDT")) {
				ItemInfo resultItem = new ItemInfo();
				resultItem.setSymbol(symbol.getSymbol());
				resultItem.setCurrentlyInTrade(false);
				resultItem.setLockStart(0);
				resultItem.setPricePrecision(symbol.getPricePrecision().intValue());
				resultItem.setQuantityPrecision(symbol.getQuantityPrecision().intValue());
				result.put(symbol.getSymbol(), resultItem);
			}
		}

		symbols = null;

		return result;
	}

	@Override
	public List<Kline> getHistoricKlines(String symbol, String interval, int timePeriod, long currentTime) {
		CandlestickInterval intervalLength = getCandlestickInterval(interval);

		SyncRequestClient client = SyncRequestClient.create();
		int limit = timePeriod;
		List<Candlestick> rawData = client.getCandlestick(symbol, intervalLength, null, null, limit);

		List<Kline> result = new ArrayList<Kline>();
		Kline dataItem;
		for (Candlestick kline : rawData) {
			dataItem = new Kline();
			dataItem.setOpenTime(kline.getOpenTime());
			dataItem.setClose(kline.getClose());
			dataItem.setOpen(kline.getOpen());
			dataItem.setHigh(kline.getHigh());
			dataItem.setLow(kline.getLow());
			dataItem.setVolume(kline.getVolume());
			result.add(dataItem);
			dataItem = null;
		}

		return result;
	}

	@Override
	public TradeVO postTrade(BigDecimal price, BigDecimal quantity, String position, String orderType,
			ItemInfo itemInfo, BotDefinition bot) throws DataException {
		Order order;

		if (price != null && quantity != null) {
			price.setScale(itemInfo.getPricePrecision(), RoundingMode.CEILING);
			quantity.setScale(itemInfo.getQuantityPrecision(), RoundingMode.FLOOR);
		} else {
			throw new DataException();
		}

		if (orderType.equals(SystemConstants.LIMIT_ORDER)) {
			// limit order submitted. Only post.
			order = getClient().postOrder(itemInfo.getSymbol(),
					position.equalsIgnoreCase(SystemConstants.SELL_SIDE) ? OrderSide.SELL : OrderSide.BUY,
					PositionSide.BOTH, OrderType.LIMIT, TimeInForce.GTC, quantity.toPlainString(),
					price.toPlainString(), null, null, null, null);
		} else {
			// market order has been sent. first post the order
			order = getClient().postOrder(itemInfo.getSymbol(),
					position.equalsIgnoreCase(SystemConstants.SELL_SIDE) ? OrderSide.SELL : OrderSide.BUY,
					PositionSide.BOTH, OrderType.MARKET, null, quantity.toPlainString(), null, null, null, null, null);

			final long entryOrderId = order.getOrderId();
			List<MyTrade> accountTrades = client.getAccountTrades(itemInfo.getSymbol(), null, null, null, 2);
			accountTrades.removeIf(item -> item.getOrderId() != entryOrderId);
			// query order status to get other data.
			MyTrade lastTrade = accountTrades.get(0);
			order = client.getOrder(itemInfo.getSymbol(), order.getOrderId(), order.getClientOrderId());
			order.setPrice(lastTrade.getPrice());
		}

		TradeVO result = new TradeVO();
		result.setBotName(bot.getBotName());
		result.setStrategyName(bot.getStrategyName());
		result.setLifeCycleName(bot.getLifeCycleName());
		result.setMarketName(bot.getMarketName());
		result.setTradeItem(itemInfo.getSymbol());
		result.setTradeClientOrderId(order.getClientOrderId());
		result.setTradeOrderId(order.getOrderId());
		result.setTradePrice(order.getPrice());

		if (orderType.contains(SystemConstants.MARKET_ORDER)) {
			// market order was placed. set
			result.setTradeTime(new Timestamp(order.getUpdateTime()));
		}

		if (SystemConstants.BUY_SIDE.equalsIgnoreCase(position)) {
			// buy side trade was placed.
			result.setQuantity(quantity.abs());
		} else if (SystemConstants.SELL_SIDE.equalsIgnoreCase(position)) {
			// sell side trade was placed.
			result.setQuantity((quantity.abs()).negate());
		} else {
			throw new DataException();
		}

		return result;
	}

	@Override
	public boolean isTradeFilled(BotDefinition bot, TradeVO trade, long serverTime) throws DataException {
		Order order;
		order = client.getOrder(trade.getTradeItem(), trade.getTradeOrderId(), null);
		if (trade.getTradeOrderId() != null) {
			order = client.getOrder(trade.getTradeItem(), trade.getTradeOrderId(), null);
		} else {
			throw new DataException();
		}

		return "FILLED".equalsIgnoreCase(order.getStatus());
	}

}
