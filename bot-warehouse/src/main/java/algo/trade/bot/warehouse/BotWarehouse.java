package algo.trade.bot.warehouse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import algo.trade.bot.Bot;
import algo.trade.bot.BotDefinition;
import algo.trade.bot.beans.TradeVO;
import algo.trade.constants.SystemConstants;
import algo.trade.factory.LifecycleFactory;
import algo.trade.factory.MarketFactory;
import algo.trade.market.beans.ItemInfo;
import algo.trade.transform.service.BaseService;

/**
 * Bot factory. 1. initializes resources 2. starts all bots on single threads.
 * 
 * @author Abhinav Shetty
 */
@Component
public class BotWarehouse extends BaseService{

	/**
	 * All tickers' relevant data
	 */
	@Resource(name = "botMarketSet")
	protected Map<Integer, Map<String, ItemInfo>> relevantTickerData;

	@Resource(name = "allBots")
	protected Map<Integer, Bot> allBots;

	@Resource(name = "botTrades")
	protected Map<Integer, List<TradeVO>> botTrades;

	/**
	 * Executor pool for the overall process
	 */
	@Resource(name = "botPool")
	protected Map<Integer, ExecutorService> botPool;

	@Autowired
	protected MarketFactory marketFactory;

	@Autowired
	protected LifecycleFactory lifecycleFactory;

	private List<Future<Boolean>> taskResultSet = Collections.synchronizedList(new ArrayList<Future<Boolean>>());

	/**
	 * initializes resources used in the bot called after application context is
	 * constructed and before application is ready.
	 */
	@PostConstruct
	public final void initializeBots() {
		// initialize all user trade and profile resources
		
		List<BotDefinition> bots = new ArrayList<BotDefinition>();
		BotDefinition firstBot = new BotDefinition("CA", "VOE", "BINANCE_FUTURES", "QHL", new BigDecimal(300), new BigDecimal(300), "USDT", null, SystemConstants.RUNNING);
		bots.add(firstBot);
		
		for (BotDefinition bot : bots) {
			
		}

		LOG.info("All trading bots initialized! ");
	}

	@Scheduled(fixedRate = 300000, initialDelay = 5000)
	public final void createAndStartAllBots() throws InterruptedException {
		for (Integer botId : allBots.keySet()) {
			LOG.info("Starting AP bot : " + allBots.get(botId).getBotDefinition());
			taskResultSet.add(botPool.get(botId).submit(allBots.get(botId)));
		}

		Date now = new Date();

		while (taskResultSet.size() > 0) {
			taskResultSet.removeIf(item -> item.isDone());
		}
	}

}
