package algo.trade.decision.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	public Boolean shouldBotOpenLongPosition(@RequestBody EntryDecisionQuery request) {
		LOG.debug("Received request to decide if long buy cycle can be executed : " + request);
		try {
			Boolean result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotOpenLongPosition(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return false;
	}

	@PostMapping(path = "/shouldBotExtendLongPosition", consumes = "application/json", produces = "application/json")
	public Boolean shouldBotExtendLongPosition(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to decide if corrective action is needed in long buy cycle : " + request);
		try {
			Boolean result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotExtendLongPosition(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return false;
	}

	@PostMapping(path = "/shouldBotPerformLongStopLossAction", consumes = "application/json", produces = "application/json")
	public Boolean shouldBotPerformLongStopLossAction(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to decide if partial exit can be made in long buy cycle : " + request);
		try {
			Boolean result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotPerformLongStopLossAction(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return false;
	}

	@PostMapping(path = "/shouldBotOpenShortPosition", consumes = "application/json", produces = "application/json")
	public Boolean shouldBotOpenShortPosition(@RequestBody EntryDecisionQuery request) {
		LOG.debug("Received request to decide if short sell cycle can be executed : " + request);
		try {
			Boolean result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotOpenShortPosition(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}

		return false;
	}

	@PostMapping(path = "/shouldBotExtendShortPosition", consumes = "application/json", produces = "application/json")
	public Boolean shouldBotExtendShortPosition(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to decide if price corrective action is needed in long buy cycle : " + request);
		try {
			Boolean result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotExtendShortPosition(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return false;
	}

	@PostMapping(path = "/shouldBotPerformShortStopLossAction", consumes = "application/json", produces = "application/json")
	public Boolean shouldBotPerformShortStopLossAction(@RequestBody SecondaryActionDecisionQuery request) {
		LOG.debug("Received request to decide if partial exit can be made in short sell cycle : " + request);
		try {
			Boolean result = decisionEngineFactory.getDecisionEngine(request.getBot().getStrategyName())
					.shouldBotPerformShortStopLossAction(request);
			return result;
		} catch (StrategyDoesNotExistException e) {
			LOG.error("Could not find the decision engine requested " + request.getBot().getStrategyName());
		}
		return false;
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
}
