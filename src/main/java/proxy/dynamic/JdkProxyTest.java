package proxy.dynamic;

import proxy.Animal;
import proxy.Tiger;

public class JdkProxyTest {
	
	public static void main(String[] args) {
		JdkProxy jdkProxy = new JdkProxy();
		Animal a = (Animal)jdkProxy.bind(new Tiger());
		a.run();
	}

}
