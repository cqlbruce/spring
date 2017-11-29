package test.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestEvent {
	
	public static void main(String[] args) {
		ApplicationContext  context = new AnnotationConfigApplicationContext(AppConfig.class);
		MailSender mailSender = (MailSender)context.getBean("mailSender");
		mailSender.sendMail("nihaohello");
//		HelloSpring hw = (HelloSpring)context.getBean("dd");
//		System.out.println(hw.getMsg());
//		context.close();
	}

}
