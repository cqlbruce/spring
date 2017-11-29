package test.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

public class MailSender implements ApplicationContextAware{

	private ApplicationContext applicationContext ; 
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
		this.applicationContext = applicationContext ; 
	}
	
	public void sendMail(String to ){
		System.out.println(MailSender.class + ":模拟发送邮件:");
		ApplicationEvent applicationEvent = new MailSenderEvent(applicationContext , to);
		applicationContext.publishEvent(applicationEvent);
	}
	
}
