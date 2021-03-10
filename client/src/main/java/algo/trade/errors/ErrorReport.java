package algo.trade.errors;

import java.io.Serializable;

import algo.trade.market.beans.ItemInfo;
/**
 * Error report wrapper. Used to communicate with admin about issues.
 * @author Abhinav Shetty
 */
public class ErrorReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3075860936706720381L;
	
	private String errorMessage;
	private ItemInfo itemInfo;
	
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the itemInfo
	 */
	public ItemInfo getItemInfo() {
		return itemInfo;
	}
	/**
	 * @param itemInfo the itemInfo to set
	 */
	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}
}
