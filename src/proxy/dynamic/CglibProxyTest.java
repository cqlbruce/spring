package proxy.dynamic;

import proxy.Tiger;

public class CglibProxyTest {
	
	public static void main(String[] args) {
		CglibProxy cp = new CglibProxy();
		Tiger t = (Tiger)cp.getInstance(new Tiger());
		t.run();
	}

}
