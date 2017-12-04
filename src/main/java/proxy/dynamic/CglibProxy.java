package proxy.dynamic;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class CglibProxy implements MethodInterceptor{

	private Object target ; 
	
	public Object getInstance(Object target){
		
		this.target = target ; 
		Enhancer enhancer = new Enhancer();
		System.out.println(this.target.getClass());
		enhancer.setSuperclass(this.target.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		System.out.println("begin....");
		proxy.invokeSuper(obj, args);
		System.out.println("end...");
		return null;
	}

	
	
}
