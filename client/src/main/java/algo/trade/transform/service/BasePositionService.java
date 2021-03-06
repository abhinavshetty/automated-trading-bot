package algo.trade.transform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import algo.trade.bot.beans.TradeVO;
import algo.trade.errors.PositionOpenException;

/**
 * Base transformation functions
 * 
 * @author Abhinav Shetty
 *
 */
@Service
public class BasePositionService extends BaseService {

	/**
	 * gets the weighted average side price for the position enter side as BUY/SELL
	 * to get specific side.
	 * 
	 * @return
	 * @throws PositionOpenException
	 *             if unable to complete the operation
	 */
	public final BigDecimal getWeightedAverageSidePriceForPosition(List<TradeVO> trades, String side)
			throws PositionOpenException {
		BigDecimal totalFiat = BigDecimal.ZERO;
		BigDecimal totalAssetQty = BigDecimal.ZERO;
		BigDecimal result = BigDecimal.ZERO;
		if (trades != null) {
			for (TradeVO trade : trades) {
				BigDecimal sidePrice = trade.getTradePrice();
				if (trade.getTradeTime() != null) {
					totalFiat = totalFiat.add(sidePrice.multiply(trade.getQuantity()));
					totalAssetQty = totalAssetQty.add(trade.getQuantity());
				} else {
					// the position is open, and an exit price cannot be computed.
					throw new PositionOpenException();
				}
			}
			result = totalFiat.divide(totalAssetQty);
		}

		trades = null;
		totalFiat = null;
		totalAssetQty = null;

		return result;
	}

	/**
	 * gets the total fiat at risk in the position
	 * 
	 * @return
	 */
	public final BigDecimal getTotalFiatInTrade(List<TradeVO> trades) {
		BigDecimal totalFiat = BigDecimal.ZERO;
		if (trades != null) {
			for (TradeVO trade : trades) {
				totalFiat = totalFiat.add((trade.getTradePrice()).multiply(trade.getQuantity()));
			}
		}
		trades = null;

		return totalFiat;
	}

	/**
	 * Computes total return from all item sets in a given set
	 * @param trades
	 * @return absolute return in fiat for the given set.
	 * @throws PositionOpenException
	 *             if unable to perform the operation
	 */
	public BigDecimal getTotalReturnFromTradesInList(List<TradeVO> trades) throws PositionOpenException {
		Map<String, List<TradeVO>> distinctItems = new ConcurrentHashMap<String, List<TradeVO>>();

		for (TradeVO trade : trades) {
			if (distinctItems.get(trade.getTradeItem()) == null) {
				distinctItems.put(trade.getTradeItem(), new ArrayList<TradeVO>());
			}
			distinctItems.get(trade.getTradeItem()).add(trade);
		}

		BigDecimal totalReturn = BigDecimal.ZERO;

		for (String itemName : distinctItems.keySet()) {
			for (TradeVO trade : distinctItems.get(itemName)) {
				totalReturn = totalReturn.add((trade.getTradePrice().multiply(trade.getQuantity())));
			}
		}

		return totalReturn;
	}

	/**
	 * returns total quantity of item in a position
	 * 
	 * @param trades
	 * @return
	 */
	public BigDecimal getTotalQuantityInPosition(List<TradeVO> trades) {
		BigDecimal result = BigDecimal.ZERO;

		for (TradeVO trade : trades) {
			result = result.add(trade.getQuantity());
		}
		return result;
	}
}
