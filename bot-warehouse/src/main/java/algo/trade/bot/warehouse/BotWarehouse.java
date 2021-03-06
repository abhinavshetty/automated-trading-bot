package algo.trade.bot.warehouse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
public class BotWarehouse extends BaseService {

	@Autowired
	private BotFactory botFactory;

	private List<BotDefinition> botDefinitions;

	private List<Bot> bots = new ArrayList<Bot>();

	private ExecutorService botProcessingLines = Executors.newCachedThreadPool();

	/**
	 * initializes resources used in the bot called after application context is
	 * constructed and before application is ready.
	 */
	@Scheduled(initialDelay = 10000, fixedRate = 60000)
	public final void initializeBots() {
		// initialize all bots definitions. Use databases to manage here.
		if (botDefinitions == null) {
			botDefinitions = new ArrayList<BotDefinition>();
			BotDefinition firstBot = new BotDefinition("CA", "VOE", "BINANCE_FUTURES", "QHL", new BigDecimal(300),
					new BigDecimal(300), "USDT", null, SystemConstants.RUNNING);
			botDefinitions.add(firstBot);
			for (BotDefinition botDef : botDefinitions) {
				try {
					Bot botUnit = botFactory.getBot(botDef);
					botUnit.setBotDefinition(botDef);
					bots.add(botUnit);
				} catch (BotDoesNotExistException e) {
					LOG.error("Could not get a bot for this name: " + botDef.getBotName());
				}
			}
			LOG.info("All trading bots instantiated! ");
		}
		
		LOG.info("Starting all bots iterative monitoring");
		for (Bot bot : bots) {
			botProcessingLines.submit(bot);
		}
	}

}
