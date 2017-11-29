package test.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AnnotationAdvices {
	
	@Before("execution(* test.aop.AnnotationCar.*(..))")
	public void before() {
		System.out.println("i'm advices , you cann't before me !");
	}
	
	@After("execution(* test.aop.AnnotationCar.*(..))")
	public void after() {
		System.out.println("i'm advices , i do after you !");
	}


}
