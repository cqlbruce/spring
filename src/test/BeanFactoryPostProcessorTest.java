package test;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
@Component  
public class BeanFactoryPostProcessorTest implements BeanFactoryPostProcessor{

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		BeanDefinition bd = beanFactory.getBeanDefinition("hellospring");
		System.out.println("come in postProcessBeanFactory");
		MutablePropertyValues v = bd.getPropertyValues();
		v.addPropertyValue("msg", "msg01");
	}

}
