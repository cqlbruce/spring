package test.event1;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ListListener implements ApplicationListener<ApplicationEvent>{

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof  ContentEvent){
            System.out.println("李四收到了新的内容：" + event.getSource());  
		}
	}
	
}
