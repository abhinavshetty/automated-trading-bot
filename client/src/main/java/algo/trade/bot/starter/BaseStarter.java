package algo.trade.bot.starter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

public class BaseStarter {
	
	private String fileName = "pid/pid.txt";
	
	private Logger log;
	
	public final void init(Class<?> starterClass, String[] args) {
		log = LogManager.getLogger(starterClass);
		log.info("Starting application : " + starterClass.getName());
		SpringApplication springApplication = new SpringApplication(starterClass);
        springApplication.addListeners(new ApplicationPidFileWriter(fileName));     // register PID write to spring boot. It will write PID to file
        springApplication.run(args);
	}
}
