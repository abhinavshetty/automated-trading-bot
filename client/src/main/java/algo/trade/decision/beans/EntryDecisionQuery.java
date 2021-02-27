package algo.trade.decision.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import algo.trade.bot.BotDefinition;
import algo.trade.market.beans.Kline;
import algo.trade.market.beans.RelevantItemInfoVO;
/**
 * Input data for initiation decisions
 * @author Abhinav Shetty
 *
 */
public class EntryDecisionQuery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3620844186779418510L;
	
	/**
	 * info about item
	 */
	private RelevantItemInfoVO itemInfo;
	
	/**
	 * market klines for item
	 */
	private Map<String, List<Kline>> marketData;
	
	/**
	 * current unix time 
	 */
	private long currentTime;
	
	private BotDefinition bot;
	
	public Map<String, List<Kline>> getMarketData() {
		return marketData;
	}
	public void setMarketData(Map<String, List<Kline>> marketData) {
		this.marketData = marketData;
	}
	public RelevantItemInfoVO getItemInfo() {
		return itemInfo;
	}
	public void setItemInfo(RelevantItemInfoVO itemInfo) {
		this.itemInfo = itemInfo;
	}
	public long getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	public BotDefinition getBot() {
		return bot;
	}
	public void setBot(BotDefinition bot) {
		this.bot = bot;
	}
}
