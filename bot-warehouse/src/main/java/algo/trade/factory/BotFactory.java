package algo.trade.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import algo.trade.bot.Bot;
import algo.trade.bot.BotDefinition;
import algo.trade.errors.BotDoesNotExistException;
import algo.trade.transform.service.BaseService;

@Component
public class BotFactory extends BaseService {

	private List<? extends Bot> bots;
	
	public Bot getBot(BotDefinition botDefinition) throws BotDoesNotExistException {
		for (Bot bot: bots) {
			if (bot.getBotDefinition().equals(botDefinition)) {
				return bot;
			}
		}
		throw new BotDoesNotExistException();
	}
}
