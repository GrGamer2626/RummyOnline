package me.grgamer2626;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RummyOnlineApplication {
	
	private static ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		applicationContext =  SpringApplication.run(RummyOnlineApplication.class, args);
	}
	
	public static synchronized ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
