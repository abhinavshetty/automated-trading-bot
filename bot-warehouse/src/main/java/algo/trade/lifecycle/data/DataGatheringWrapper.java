package algo.trade.lifecycle.data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import algo.trade.bot.BotDefinition;
import algo.trade.constants.SystemConstants;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.factory.MarketFactory;
import algo.trade.lifecycle.client.MarketDataWrapper;
import algo.trade.market.beans.ItemInfo;
import algo.trade.market.beans.Kline;
import algo.trade.market.client.MarketInterface;

@Service
public class DataGatheringWrapper implements MarketDataWrapper {

	private static final String[] LIFECYCLES = { "CA" };

	@Override
	public List<String> getLifeCycles() {
		return Arrays.asList(DataGatheringWrapper.LIFECYCLES);
	}

	@Autowired
	private MarketFactory marketFactory;

	@Override
	public Map<String, List<Kline>> getLongEntryMarketData(ItemInfo itemInfo, BotDefinition bot,
			long serverTime, Map<String, Object> config) throws MarketDoesNotExistException {
		Map<String, List<Kline>> result = new ConcurrentHashMap<String, List<Kline>>();
		MarketInterface client = marketFactory.getClient(bot);

		Set<String> entryKlines = config.keySet();
		entryKlines.removeIf(item -> !(item.contains(SystemConstants.ENTRY_MONITORING_KLINE_KEY)));

		for (String klineGroup : entryKlines) {
			List<Kline> klinesForGroup = client.getHistoricKlines(itemInfo.getSymbol(),
					config.get(klineGroup).toString(),
					Integer.valueOf(config.get(SystemConstants.ENTRY_MONITORING_WINDOW_HOURS_KEY).toString()),
					serverTime);
			result.put(klineGroup, klinesForGroup);
		}
		
		client = null;
		entryKlines = null;
		
		return result;
	}

	@Override
	public Map<String, List<Kline>> getShortEntryMarketData(ItemInfo itemInfo, BotDefinition bot,
			long serverTime, Map<String, Object> config) throws MarketDoesNotExistException {
		Map<String, List<Kline>> result = new ConcurrentHashMap<String, List<Kline>>();
		MarketInterface client = marketFactory.getClient(bot);

		Set<String> entryKlines = config.keySet();
		entryKlines.removeIf(item -> !(item.contains(SystemConstants.ENTRY_MONITORING_KLINE_KEY)));

		for (String klineGroup : entryKlines) {
			List<Kline> klinesForGroup = client.getHistoricKlines(itemInfo.getSymbol(),
					config.get(klineGroup).toString(),
					Integer.valueOf(config.get(SystemConstants.ENTRY_MONITORING_WINDOW_HOURS_KEY).toString()),
					serverTime);
			result.put(klineGroup, klinesForGroup);
		}
		
		client = null;
		entryKlines = null;
		
		return result;
	}

	@Override
	public Map<String, List<Kline>> getLongCorrectionMarketData(ItemInfo itemInfo, BotDefinition bot,
			long serverTime, Map<String, Object> config) throws MarketDoesNotExistException {
		Map<String, List<Kline>> result = new ConcurrentHashMap<String, List<Kline>>();
		MarketInterface client = marketFactory.getClient(bot);

		Set<String> entryKlines = config.keySet();
		entryKlines.removeIf(item -> !(item.contains(SystemConstants.EXTENSION_MONITORING_KLINE_KEY)));

		for (String klineGroup : entryKlines) {
			List<Kline> klinesForGroup = client.getHistoricKlines(itemInfo.getSymbol(),
					config.get(klineGroup).toString(),
					Integer.valueOf(config.get(SystemConstants.EXTENSION_MONITORING_WINDOW_HOURS_KEY).toString()),
					serverTime);
			result.put(klineGroup, klinesForGroup);
		}
		
		client = null;
		entryKlines = null;
		
		return result;
	}

	@Override
	public Map<String, List<Kline>> getShortCorrectionMarketData(ItemInfo itemInfo, BotDefinition bot,
			long serverTime, Map<String, Object> config) throws MarketDoesNotExistException {
		Map<String, List<Kline>> result = new ConcurrentHashMap<String, List<Kline>>();
		MarketInterface client = marketFactory.getClient(bot);

		Set<String> entryKlines = config.keySet();
		entryKlines.removeIf(item -> !(item.contains(SystemConstants.EXTENSION_MONITORING_KLINE_KEY)));

		for (String klineGroup : entryKlines) {
			List<Kline> klinesForGroup = client.getHistoricKlines(itemInfo.getSymbol(),
					config.get(klineGroup).toString(),
					Integer.valueOf(config.get(SystemConstants.EXTENSION_MONITORING_WINDOW_HOURS_KEY).toString()),
					serverTime);
			result.put(klineGroup, klinesForGroup);
		}
		
		client = null;
		entryKlines = null;
		
		return result;
	}

}
