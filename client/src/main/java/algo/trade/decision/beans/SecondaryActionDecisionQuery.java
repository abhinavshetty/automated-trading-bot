package algo.trade.decision.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.market.beans.Kline;
import algo.trade.market.beans.ItemInfo;

public class SecondaryActionDecisionQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4526655569785594131L;

	/**
	 * market klines
	 */
	private Map<String, List<Kline>> marketData;
	
	private long currentTime;
	
	/**
	 * current trades for item
	 */
	private List<TradeVO> currentOutstandingTrades;
	
	/**
	 * item info
	 */
	private ItemInfo itemInfo;
	
	private BotDefinition bot;

	public SecondaryActionDecisionQuery() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SecondaryActionDecisionQuery(Map<String, List<Kline>> marketData, long currentTime,
			List<TradeVO> currentOutstandingTrades, ItemInfo itemInfo, BotDefinition bot) {
		super();
		this.marketData = marketData;
		this.currentTime = currentTime;
		this.currentOutstandingTrades = currentOutstandingTrades;
		this.itemInfo = itemInfo;
		this.bot = bot;
	}

	public Map<String, List<Kline>> getMarketData() {
		return marketData;
	}

	public void setMarketData(Map<String, List<Kline>> marketData) {
		this.marketData = marketData;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public List<TradeVO> getCurrentOutstandingTrades() {
		return currentOutstandingTrades;
	}

	public void setCurrentOutstandingTrades(List<TradeVO> currentOutstandingTrades) {
		this.currentOutstandingTrades = currentOutstandingTrades;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	public BotDefinition getBot() {
		return bot;
	}

	public void setBot(BotDefinition bot) {
		this.bot = bot;
	}
}
