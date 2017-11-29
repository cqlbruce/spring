package test.event;

import org.springframework.context.ApplicationListener;

public class MailSenderListener implements ApplicationListener<MailSenderEvent>{

	@Override
	public void onApplicationEvent(MailSenderEvent event) {
		System.out.println(MailSenderListener.class + ":向" + event.getTo() + " 发送完一封邮件");
	}
	
}
