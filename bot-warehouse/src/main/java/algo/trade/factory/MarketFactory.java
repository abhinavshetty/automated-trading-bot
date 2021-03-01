package algo.trade.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import algo.trade.bot.BotDefinition;
import algo.trade.errors.MarketDoesNotExistException;
import algo.trade.market.client.MarketInterface;

@Component
public class MarketFactory {
	
	@Autowired
	private List<? extends MarketInterface> marketInterfaces; 
	
	public MarketInterface getClient(BotDefinition bot) throws MarketDoesNotExistException {
		
		for (MarketInterface client: marketInterfaces) {
			if (client.getMarkets().contains(bot.getMarketName())) {
				return client;
			}
		}
		throw new MarketDoesNotExistException();
	}

}
