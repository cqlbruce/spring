package test.event1;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component  
public class ZhangsanListener implements ApplicationListener<ContentEvent>{

	@Override
	public void onApplicationEvent(ContentEvent event) {
		System.out.println("张三收到了新的内容:" + event.getSource());
	}
	
}
