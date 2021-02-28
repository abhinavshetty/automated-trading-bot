package algo.trade.decision.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import algo.trade.decision.beans.DecisionResponse;
import algo.trade.decision.beans.EntryDecisionQuery;
import algo.trade.decision.beans.SecondaryActionDecisionQuery;
import algo.trade.decision.engine.DecisionEngineFactory;
import algo.trade.errors.StrategyDoesNotExistException;
import algo.trade.transform.service.BaseService;

/**
 * Rest endpoint for engine to talk to other systems.
 * 
 * @author Abhinav Shetty
 */
@RestController
public class EngineController extends BaseService {

	@Autowired
	private DecisionEngineFactory decisionEngineFactory;

	@PostMapping(path = "/shouldBotOpenLongPosition", consumes = "application/json", produces = "application/json")
	public DecisionResponse shouldBotOpenLongPosition(@RequestBody EntryDecisionQuery request) {
		DecisionResponse result;
		try {
			result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotOpenLongPosition(request);
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
			result = constructEmptyResponse();
		}
		return result;
	}

	@PostMapping(path = "/shouldBotExtendLongPosition", consumes = "application/json", produces = "application/json")
	public DecisionResponse shouldBotExtendLongPosition(@RequestBody SecondaryActionDecisionQuery request) {
		DecisionResponse result;
		try {
			result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotExtendLongPosition(request);
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
			result = constructEmptyResponse();
		}
		return result;
	}

	@PostMapping(path = "/shouldBotPerformLongStopLossAction", consumes = "application/json", produces = "application/json")
	public DecisionResponse shouldBotPerformLongStopLossAction(@RequestBody SecondaryActionDecisionQuery request) {
		DecisionResponse result;
		try {
			result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotPerformLongStopLossAction(request);
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
			result = constructEmptyResponse();
		}
		return result;
	}

	@PostMapping(path = "/shouldBotOpenShortPosition", consumes = "application/json", produces = "application/json")
	public DecisionResponse shouldBotOpenShortPosition(@RequestBody EntryDecisionQuery request) {
		DecisionResponse result;
		try {
			result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotOpenShortPosition(request);
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
			result = constructEmptyResponse();
		}

		return result;
	}

	@PostMapping(path = "/shouldBotExtendShortPosition", consumes = "application/json", produces = "application/json")
	public DecisionResponse shouldBotExtendShortPosition(@RequestBody SecondaryActionDecisionQuery request) {
		DecisionResponse result;
		try {
			result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotExtendShortPosition(request);
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
			result = constructEmptyResponse();
		}
		return result;
	}

	@PostMapping(path = "/shouldBotPerformShortStopLossAction", consumes = "application/json", produces = "application/json")
	public DecisionResponse shouldBotPerformShortStopLossAction(@RequestBody SecondaryActionDecisionQuery request) {
		DecisionResponse result;
		try {
			result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotPerformShortStopLossAction(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
			result = constructEmptyResponse();
		}
		return result;
	}

	@PostMapping(path = "/getExitSellPrice", consumes = "application/json", produces = "application/json")
	public BigDecimal getExitSellPrice(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to get exit sell price in long buy cycle : " + request);
		try {
			BigDecimal result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.getExitSellPrice(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return BigDecimal.ZERO;
	}

	@PostMapping(path = "/getExitBuyPrice", consumes = "application/json", produces = "application/json")
	public BigDecimal getExitBuyPrice(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to get exit buy price in short sell cycle : " + request);
		try {
			BigDecimal result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.getExitBuyPrice(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return BigDecimal.ZERO;
	}

	@PostMapping(path = "/getStopLossSellPrice", consumes = "application/json", produces = "application/json")
	public BigDecimal getStopLossSellPrice(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to get exit sell price in long buy cycle : " + request);
		try {
			BigDecimal result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.getStopLossSellPrice(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return BigDecimal.ZERO;
	}

	@PostMapping(path = "/getStopLossBuyPrice", consumes = "application/json", produces = "application/json")
	public BigDecimal getStopLossBuyPrice(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to get exit buy price in short sell cycle : " + request);
		try {
			BigDecimal result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.getStopLossBuyPrice(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return BigDecimal.ZERO;
	}
	
	@PostMapping(path = "/getBotConfigurationConstants", consumes = "application/json", produces = "application/json")
	public DecisionResponse getBotConfigurationConstants(@RequestBody EntryDecisionQuery request) {
		DecisionResponse result = constructEmptyResponse();
		try {
			result.setConfigParameters(decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.getBotConfigurationConstants());
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return result;
	}

	private DecisionResponse constructEmptyResponse() {
		DecisionResponse result = new DecisionResponse();
		result.setShouldBotActOnItem(false);
		return result;
	}
}
