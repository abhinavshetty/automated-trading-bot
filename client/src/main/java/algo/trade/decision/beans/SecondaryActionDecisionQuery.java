package algo.trade.decision.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import algo.trade.beans.TradeVO;
import algo.trade.market.beans.Kline;
import algo.trade.market.beans.RelevantItemInfoVO;

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
	private RelevantItemInfoVO itemInfo;
	

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

	public RelevantItemInfoVO getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(RelevantItemInfoVO itemInfo) {
		this.itemInfo = itemInfo;
	}
}
