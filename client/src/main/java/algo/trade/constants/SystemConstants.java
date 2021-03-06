package algo.trade.constants;

public class SystemConstants {

	public static final int OVERALL_POOL_SIZE = 2;

	public static final int MARKET_POOL_SIZE = 5;

	public static final int APPLICATION_DB_POOL_SIZE = 5;

	public static final int LONG_TRADE = 1;
	public static final int SHORT_TRADE = 2;

	public static final String LONG_POSITION = "LONG";
	public static final String SHORT_POSITION = "SHORT";

	public static final String BUY_SIDE = "BUY";

	public static final String SELL_SIDE = "SELL";

	public static final String LIMIT_ORDER = "LIMIT";
	public static final String MARKET_ORDER = "MARKET";

	// keys for bot running state
	public static final String RUNNING = "TRADING";
	public static final String OFFLINE = "OFFLINE";
	public static final String HOLD = "HOLD";

	// keys for bot and market configuration
	public static final String INITIAL_TRADE_MARGIN_KEY = "INITIAL_TRADE_MARGIN";
	public static final String EXTENSION_TRADE_MARGIN_KEY = "EXTENSION_TRADE_MARGIN";
	public static final String ENTRY_MONITORING_KLINE_KEY = "ENTRY_MONITORING_KLINE";
	public static final String ENTRY_MONITORING_WINDOW_HOURS_KEY = "ENTRY_MONITORING_WINDOW_HOURS";
	public static final String EXTENSION_MONITORING_KLINE_KEY = "EXTENSION_MONITORING_KLINE";
	public static final String EXTENSION_MONITORING_WINDOW_HOURS_KEY = "EXTENSION_MONITORING_WINDOW_HOURS";
	public static final String MINIMUM_ITEM_TRADE_HISTORY_DAYS_KEY = "MINIMUM_ITEM_TRADE_HISTORY_DAYS";

	// keys for decision taking
	public static final String DECISION_TAKING_ACTION_KEY = "DECISION_TAKING_ACTION";
	public static final String LONG_ENTRY_ACTION = "LONG_ENTRY";
	public static final String LONG_EXTEND_ACTION = "LONG_EXTEND";
	public static final String LONG_STOP_LOSS_ACTION = "LONG_STOP_LOSS";
	public static final String SHORT_ENTRY_ACTION = "SHORT_ENTRY";
	public static final String SHORT_EXTEND_ACTION = "SHORT_EXTEND";
	public static final String SHORT_STOP_LOSS_ACTION = "SHORT_STOP_LOSS";
	public static final String MAX_PORTFOLIO_SIZE_KEY = "MAX_PORTFOLIO_SIZE";
	public static final String COMPARISON_INDEX_KEY = "COMPARISON_INDEX";
	public static final String LONG_PROFIT_KEY = "LONG_PROFIT";
	public static final String LONG_EXTENSION_THRESHOLD_KEY = "LONG_EXTENSION_THRESHOLD";
	public static final String LONG_STOP_LOSS_THRESHOLD_KEY = "LONG_STOP_LOSS_THRESHOLD";
	public static final String SHORT_PROFIT_KEY = "SHORT_PROFIT";
	public static final String SHORT_EXTENSION_THRESHOLD_KEY = "SHORT_EXTENSION_THRESHOLD";
	public static final String SHORT_STOP_LOSS_THRESHOLD_KEY = "SHORT_STOP_LOSS_THRESHOLD";
	public static final String ITEM_INFO_KEY = "ITEM_INFO";
	
	// keys for decision logging
	public static final String ADD_TRADES = "ADD";
	public static final String UPDATE_TRADES = "UPDATE";
	public static final String DELETE_TRADES = "DELETE";

	// time periods
	public static final String ONE_DAY = "1d";
	public static final String TWELVE_HOURS = "12h";
	public static final String SIX_HOURS = "6h";
	public static final String FOUR_HOURS = "4h";
	public static final String TWO_HOURS = "2h";
	public static final String ONE_HOUR = "1h";
	public static final String HALF_HOUR = "30m";
	public static final String QUARTER_HOUR = "15m";
	public static final String FIVE_MINUTES = "5m";
	public static final String ONE_MINUTE = "1m";

	public static final String POSITION_CLOSE_ACTION = "POSITION_CLOSE";

	public static final Object UPDATED_POSITIONS_KEY = "POSITIONS_UPDATED";
}
