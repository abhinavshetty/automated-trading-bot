package algo.trade.decision.engine;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import algo.trade.decision.client.DecisionEngine;
import algo.trade.errors.StrategyDoesNotExistException;

/**
 * Decision engine factory
 * @author Abhinav Shetty
 *
 */
@Component
public class DecisionEngineFactory {
	
	@Autowired
	private List<? extends DecisionEngine> decisionEngines;
	
	public DecisionEngine getDecisionEngine(String strategyName) throws StrategyDoesNotExistException {
		for (DecisionEngine engine : decisionEngines) {
			if (Arrays.asList(engine.getStrategies()).contains(strategyName)) {
				return engine;
			}
		}
		throw new StrategyDoesNotExistException();
	}
}
