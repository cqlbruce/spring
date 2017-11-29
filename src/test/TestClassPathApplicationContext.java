package test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.aop.AnnotationCar;

public class TestClassPathApplicationContext {
	private final static String path = "classpath:applicationContext.xml"; 
	

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext cpa = new ClassPathXmlApplicationContext();
		//设置生成�?发环境变量，通过xml的配置来搞定
		cpa.getEnvironment().setActiveProfiles("product");
		//
		cpa.setConfigLocation(path);
		cpa.refresh();
		cpa.start();
//		HelloSpring hw = (HelloSpring)cpa.getBean("hellospring");
		HelloSpring hw = (HelloSpring)cpa.getBean("dd");
		System.out.println(hw.getMsg());
		
//		Car c = (Car)cpa.getBean("car");
//		c.run();
//		c.dodo();
		AnnotationCar ac = (AnnotationCar)cpa.getBean("annotationCar");
		ac.dodo();
		
	}
	
}
