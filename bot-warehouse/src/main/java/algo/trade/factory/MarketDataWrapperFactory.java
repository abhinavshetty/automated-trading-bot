package algo.trade.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import algo.trade.bot.BotDefinition;
import algo.trade.lifecycle.client.MarketDataWrapper;

@Component
public class MarketDataWrapperFactory {

	@Autowired
	private List<? extends MarketDataWrapper> marketClients; 
	
	public MarketDataWrapper getDataWrapperForLifeCycle(BotDefinition bot) {
		
		for (MarketDataWrapper client: marketClients) {
			if (client.getLifeCycles().contains(bot.getLifeCycleName())) {
				return client;
			}
		}
		return null;
	}
}
