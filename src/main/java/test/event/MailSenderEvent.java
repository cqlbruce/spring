package test.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class MailSenderEvent extends ApplicationContextEvent{

	private String to ; 
	
	public MailSenderEvent(ApplicationContext source , String to){
		super(source);
		this.to = to ;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
}
