package test.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public ApplicationListener applicationListener(){
		return new MailSenderListener();
	}
	
	@Bean
	public MailSender mailSender(){
		return new MailSender();
	}
	
	
}
