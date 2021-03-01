package algo.trade.bot.warehouse.startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import algo.trade.bot.starter.BaseStarter;

/**
 * Microservice for bot warehouse
 * @author Abhinav Shetty
 */
@SpringBootApplication
@ComponentScan(basePackages = "algo.trade")
@EnableAutoConfiguration
@EnableScheduling
public class BotWarehouseStartPoint {

	public static final Logger LOG = LogManager.getLogger(BotWarehouseStartPoint.class);

	public static void main(String[] args) {
		BaseStarter starter = new BaseStarter();
		starter.init(BotWarehouseStartPoint.class, args);
		LOG.info("Started application : " + BotWarehouseStartPoint.class);
	}
	
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("http://localhost:48111");
//			}
//		};
//	}
}
