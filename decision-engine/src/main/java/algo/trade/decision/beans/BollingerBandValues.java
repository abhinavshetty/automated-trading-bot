package algo.trade.decision.beans;

import java.math.BigDecimal;

/**
 * Wrapper for Bollinger bands calculated
 * @author Abhinav Shetty
 *
 */
public class BollingerBandValues {

	private BigDecimal[] upperBand;
	private BigDecimal[] lowerBand;
	private BigDecimal[] middleBand;
	
	public BigDecimal[] getUpperBand() {
		return upperBand;
	}
	public void setUpperBand(BigDecimal[] upperBand) {
		this.upperBand = upperBand;
	}
	public BigDecimal[] getLowerBand() {
		return lowerBand;
	}
	public void setLowerBand(BigDecimal[] lowerBand) {
		this.lowerBand = lowerBand;
	}
	public BigDecimal[] getMiddleBand() {
		return middleBand;
	}
	public void setMiddleBand(BigDecimal[] middleBand) {
		this.middleBand = middleBand;
	}
	
}
