package proxy.staticc;

import proxy.Tiger;

public class TestProxy {
	
	public static void main(String[] args) {
		
		Tiger t = new Tiger();
		TigerProxy tp = new TigerProxy(t);
		tp.say();
	}

}
