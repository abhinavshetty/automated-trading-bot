package algo.trade.bot.warehouse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import algo.trade.bot.Bot;
import algo.trade.bot.BotDefinition;
import algo.trade.constants.SystemConstants;
import algo.trade.errors.BotDoesNotExistException;
import algo.trade.factory.BotFactory;
import algo.trade.transform.service.BaseService;

/**
 * Bot factory. 1. initializes resources 2. starts all bots on single threads.
 * 
 * @author Abhinav Shetty
 */
@Component
public class BotWarehouse extends BaseService{
	
	@Autowired
	private BotFactory botFactory;
	
	private List<Boolean> botResult = new ArrayList<Boolean>();
	
	private ExecutorService botProcessingLines = Executors.newCachedThreadPool();

	/**
	 * initializes resources used in the bot called after application context is
	 * constructed and before application is ready.
	 */
	@PostConstruct
	public final void initializeBots() {
		// initialize all user trade and profile resources
		
		List<BotDefinition> bots = new ArrayList<BotDefinition>();
		BotDefinition firstBot = new BotDefinition("CA", "VOE", "BINANCE_FUTURES", "QHL", new BigDecimal(300), new BigDecimal(300), "USDT", null, SystemConstants.RUNNING);
//		BotDefinition secondBot = new BotDefinition("CA", "VOE", "BINANCE_FUTURES", "QHL", new BigDecimal(300), new BigDecimal(200), "USDT", null, SystemConstants.RUNNING);
		bots.add(firstBot);
//		bots.add(secondBot);
		
		for (BotDefinition bot : bots) {
			try {
				Bot botUnit = botFactory.getBot(bot);
				botUnit.setBotDefinition(bot);
				botProcessingLines.submit(botUnit);
				
			} catch (BotDoesNotExistException e) {
				LOG.error("Could not get a bot for this name: " + bot.getBotName());
			}
		}
		

		LOG.info("All trading bots initialized! ");
	}

	public final void createAndStartAllBots() throws InterruptedException {
//		for (Integer botId : allBots.keySet()) {
//			LOG.info("Starting AP bot : " + allBots.get(botId).getBotDefinition());
//			taskResultSet.add(botPool.get(botId).submit(allBots.get(botId)));
//		}
//
//		Date now = new Date();
//
//		while (taskResultSet.size() > 0) {
//			taskResultSet.removeIf(item -> item.isDone());
//		}
	}

}
