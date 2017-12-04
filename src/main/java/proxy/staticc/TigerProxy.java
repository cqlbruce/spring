package proxy.staticc;

import proxy.Animal;

public class TigerProxy implements Animal{
	
	private Animal a ;
	
	public TigerProxy(Animal a ){
		this.a = a ; 
	}

	@Override
	public void say() {
		System.out.println("tiger say something begin !");
		a.say();
		System.out.println("tiger say something end !");
	}

	@Override
	public void run() {
		System.out.println("tiger say something begin !");
		a.run();
		System.out.println("tiger say something end !");
	}
	
	

}
