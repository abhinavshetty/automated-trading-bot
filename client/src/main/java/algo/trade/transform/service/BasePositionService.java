package algo.trade.transform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import algo.trade.bot.beans.TradeVO;
import algo.trade.constants.SystemConstants;
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
				BigDecimal sidePrice = SystemConstants.BUY_SIDE.equalsIgnoreCase(side) ? trade.getBuyPrice()
						: trade.getSellPrice();
				if (sidePrice != null) {
					totalFiat = totalFiat.add(sidePrice.multiply(trade.getQuantity()));
					totalAssetQty = totalAssetQty.add(trade.getQuantity());
				} else {
					// the position is open
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
				totalFiat = totalFiat.add(((trade.getTradeSide() == SystemConstants.LONG_TRADE ? trade.getBuyPrice()
						: trade.getSellPrice()).multiply(trade.getQuantity())));
			}
		}
		trades = null;

		return totalFiat;
	}

	/**
	 * Computes total return from all item sets in a given set
	 * 
	 * @param trades
	 * @return
	 * @throws PositionOpenException
	 *             if unable to perform the operation
	 */
	public BigDecimal getTotalReturnFromTradesInList(List<TradeVO> trades) throws PositionOpenException {
		Map<String, List<TradeVO>> distinctCurrencies = new ConcurrentHashMap<String, List<TradeVO>>();

		for (TradeVO trade : trades) {
			if (distinctCurrencies.get(trade.getSellClientOrderId()) == null) {
				distinctCurrencies.put(trade.getSellClientOrderId(), new ArrayList<TradeVO>());
			}
			distinctCurrencies.get(trade.getSellClientOrderId()).add(trade);
		}

		BigDecimal currencyReturn = BigDecimal.ZERO;

		for (String orderSet : distinctCurrencies.keySet()) {
			BigDecimal buyPriceForSet = this.getWeightedAverageSidePriceForPosition(distinctCurrencies.get(orderSet),
					SystemConstants.BUY_SIDE);
			BigDecimal sellPriceForSet = this.getWeightedAverageSidePriceForPosition(distinctCurrencies.get(orderSet),
					SystemConstants.SELL_SIDE);
			BigDecimal buyQuantity = this.getTotalFiatInTrade(distinctCurrencies.get(orderSet));

			currencyReturn = currencyReturn.add((sellPriceForSet.subtract(buyPriceForSet)).divide(buyPriceForSet))
					.multiply(buyQuantity);
		}

		return currencyReturn;
	}

	/**
	 * returns total quantity of item in a position
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
