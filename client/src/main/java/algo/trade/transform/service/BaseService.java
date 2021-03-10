package algo.trade.transform.service;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base service - implements common logger
 * @author Abhinav Shetty
 *
 */
public abstract class BaseService {

	protected Logger LOG;
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	@PostConstruct()
	protected Logger LOGGER() {
		if (this.LOG == null) {
			LOG = LogManager.getLogger(this.getClass());
			LOG.info("Created logger for class : " + this.getClass());
		}
		return this.LOG;
	};
	
	
}
