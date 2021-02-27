package algo.trade.decision.startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import algo.trade.bot.starter.BaseStarter;

/**
 * Decision microservice
 * @author Abhinav Shetty
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "algo.trade")
@EnableAutoConfiguration
public class DecisionEngineStartPoint {

	public static final Logger LOG = LogManager.getLogger(DecisionEngineStartPoint.class);

	public static void main(String[] args) {
		BaseStarter starter = new BaseStarter();
		starter.init(DecisionEngineStartPoint.class, args);
		LOG.info("Started application : " + DecisionEngineStartPoint.class);
	}
}

