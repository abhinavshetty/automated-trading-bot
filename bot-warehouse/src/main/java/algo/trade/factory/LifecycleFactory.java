package algo.trade.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import algo.trade.errors.LifeCycleDoesNotExistException;
import algo.trade.lifecycle.client.LifeCycle;

/**
 * Returns managed life cycle implementations
 * @author Abhinav Shetty
 *
 */
@Component
public class LifecycleFactory {

	@Autowired
	private List<? extends LifeCycle> lifecycles;
	
	public LifeCycle getLifecycle(String lifecycleName) throws LifeCycleDoesNotExistException {
		for (LifeCycle manager : lifecycles) {
			if (manager.getLifeCycle().equalsIgnoreCase(lifecycleName)) {
				return manager;
			}
		}
		throw new LifeCycleDoesNotExistException();
	}
}
