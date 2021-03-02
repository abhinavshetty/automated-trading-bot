package algo.trade.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import algo.trade.bot.Bot;
import algo.trade.bot.BotDefinition;
import algo.trade.bot.QuarterHourlyLookerBot;
import algo.trade.decision.client.DecisionEngineClient;
import algo.trade.errors.BotDoesNotExistException;
import algo.trade.transform.service.BaseService;

@Component
public class BotFactory extends BaseService {
	
	@Autowired
	private LifecycleFactory lifeCycleFactory;
	
	@Autowired
	private MarketFactory marketFactory;
	
	@Autowired
	private DecisionEngineClient engineClient;
	
	public Bot getBot(BotDefinition botDefinition) throws BotDoesNotExistException {
		switch (botDefinition.getBotName()) {
		case "QHL": 
			QuarterHourlyLookerBot result = new QuarterHourlyLookerBot();
			result.instantiateBot(marketFactory, lifeCycleFactory, engineClient, botDefinition);
			return result;
		
		default:
			throw new BotDoesNotExistException();
		}
	}
}
