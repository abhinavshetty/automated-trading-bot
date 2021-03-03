package algo.trade.decision.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for Bollinger bands calculated
 * @author Abhinav Shetty
 *
 */
public class BollingerBandValues {

	private List<BigDecimal> upperBand;
	private List<BigDecimal> lowerBand;
	private List<BigDecimal> middleBand;
	
	public BollingerBandValues() {
		super();
		upperBand = new ArrayList<BigDecimal>();
		middleBand = new ArrayList<BigDecimal>();
		lowerBand = new ArrayList<BigDecimal>();
	}
	public List<BigDecimal> getUpperBand() {
		return upperBand;
	}
	public void setUpperBand(List<BigDecimal> upperBand) {
		this.upperBand = upperBand;
	}
	public List<BigDecimal> getLowerBand() {
		return lowerBand;
	}
	public void setLowerBand(List<BigDecimal> lowerBand) {
		this.lowerBand = lowerBand;
	}
	public List<BigDecimal> getMiddleBand() {
		return middleBand;
	}
	public void setMiddleBand(List<BigDecimal> middleBand) {
		this.middleBand = middleBand;
	}
	
}
