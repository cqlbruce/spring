package test.aop;

import org.springframework.stereotype.Service;

@Service("annotationCar")
public class AnnotationCar {
	
	public void run() {
		System.out.println("i'm a car , i'm running...");
	}

	public void dodo() {
		System.out.println("watch out , do do ...");
	}
	
	public void stop() {
		System.out.println("i cann't run , i want to stop !");
	}
}
