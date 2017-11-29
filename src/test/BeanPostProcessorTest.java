package test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component  
public class BeanPostProcessorTest implements BeanPostProcessor{

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanName : " + beanName +" ,coming postProcessBeforeInitialization");
		
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanName : " + beanName +" ,coming postProcessAfterInitialization");
		
		return bean;
	}

	
	
}
