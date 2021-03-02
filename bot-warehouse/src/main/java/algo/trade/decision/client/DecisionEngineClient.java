package algo.trade.decision.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.common.config.BaseRestService;
import algo.trade.decision.beans.DecisionResponse;
import algo.trade.decision.beans.EntryDecisionQuery;
import algo.trade.decision.beans.SecondaryActionDecisionQuery;
import algo.trade.market.beans.ItemInfo;
import algo.trade.market.beans.Kline;
import algo.trade.transform.service.BaseService;

/**
 * Interface between bot warehouse and decision engine
 * 
 * @author Abhinav Shetty
 */
@Service
public class DecisionEngineClient extends BaseService {

	@Autowired
	private BaseRestService restService;

	@Value("${determination.engine.location}")
	private String determinationEngineUrl;

	public Map<String, Object> getBotConfigurationConstants(EntryDecisionQuery request) {
		String url = determinationEngineUrl + "/getBotConfigurationConstants";
		DecisionResponse result = restService.postWithSingleInputForSingleOutput(request, null, url,
				DecisionResponse.class);
		return result.getConfigParameters();
	}

	public DecisionResponse shouldBotOpenLongPosition(ItemInfo itemInfo, Map<String, List<Kline>> marketData,
			BotDefinition bot, long currentTime) {
		EntryDecisionQuery request = new EntryDecisionQuery(itemInfo, marketData, currentTime, bot);
		String url = determinationEngineUrl + "/shouldBotOpenLongPosition";
		DecisionResponse result = restService.postWithSingleInputForSingleOutput(request, null, url,
				DecisionResponse.class);
		return result;
	}

	public DecisionResponse shouldBotExtendLongPosition(ItemInfo itemInfo, Map<String, List<Kline>> marketData,
			BotDefinition bot, List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/shouldBotExtendLongPosition";
		DecisionResponse result = restService.postWithSingleInputForSingleOutput(request, null, url,
				DecisionResponse.class);
		return result;
	}

	public DecisionResponse shouldBotPerformLongStopLossAction(ItemInfo itemInfo, Map<String, List<Kline>> marketData,
			BotDefinition bot, List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/shouldBotPerformLongStopLossAction";
		DecisionResponse result = restService.postWithSingleInputForSingleOutput(request, null, url,
				DecisionResponse.class);
		return result;
	}

	public DecisionResponse shouldBotOpenShortPosition(ItemInfo itemInfo, Map<String, List<Kline>> marketData,
			BotDefinition bot, long currentTime) {
		EntryDecisionQuery request = new EntryDecisionQuery(itemInfo, marketData, currentTime, bot);
		String url = determinationEngineUrl + "/shouldBotOpenShortPosition";
		DecisionResponse result = restService.postWithSingleInputForSingleOutput(request, null, url,
				DecisionResponse.class);
		return result;
	}

	public DecisionResponse shouldBotExtendShortPosition(ItemInfo itemInfo, Map<String, List<Kline>> marketData,
			BotDefinition bot, List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/shouldBotExtendShortPosition";
		DecisionResponse result = restService.postWithSingleInputForSingleOutput(request, null, url,
				DecisionResponse.class);
		return result;
	}

	public DecisionResponse shouldBotPerformShortStopLossAction(ItemInfo itemInfo, Map<String, List<Kline>> marketData,
			BotDefinition bot, List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/shouldBotPerformShortStopLossAction";
		DecisionResponse result = restService.postWithSingleInputForSingleOutput(request, null, url,
				DecisionResponse.class);
		return result;
	}

	public BigDecimal getExitSellPrice(ItemInfo itemInfo, Map<String, List<Kline>> marketData, BotDefinition bot,
			List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/getExitSellPrice";
		BigDecimal result = restService.postWithSingleInputForSingleOutput(request, null, url, BigDecimal.class);
		return result;
	}

	public BigDecimal getExitBuyPrice(ItemInfo itemInfo, Map<String, List<Kline>> marketData, BotDefinition bot,
			List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/getExitBuyPrice";
		BigDecimal result = restService.postWithSingleInputForSingleOutput(request, null, url, BigDecimal.class);
		return result;
	}

	public BigDecimal getStopLossSellPrice(ItemInfo itemInfo, Map<String, List<Kline>> marketData, BotDefinition bot,
			List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/getStopLossSellPrice";
		BigDecimal result = restService.postWithSingleInputForSingleOutput(request, null, url, BigDecimal.class);
		return result;
	}

	public BigDecimal getStopLossBuyPrice(ItemInfo itemInfo, Map<String, List<Kline>> marketData, BotDefinition bot,
			List<TradeVO> currentOutstandingTrades, long currentTime) {
		SecondaryActionDecisionQuery request = new SecondaryActionDecisionQuery(marketData, currentTime,
				currentOutstandingTrades, itemInfo, bot);
		String url = determinationEngineUrl + "/getStopLossBuyPrice";
		BigDecimal result = restService.postWithSingleInputForSingleOutput(request, null, url, BigDecimal.class);
		return result;
	}

}
