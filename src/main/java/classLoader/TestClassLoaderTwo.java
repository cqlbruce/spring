package classLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class TestClassLoaderTwo {
	
	
	public static void main(String[] args) {
		try {
			String urlStr = "META-INF/spring.schemas" ; 
			ClassLoader c1 = ClassLoader.getSystemClassLoader();
			Enumeration<URL> u1 = c1.getResources(urlStr);
			System.out.println("u1.....");
			iterator(u1);
			ClassLoader c2 = Thread.currentThread().getContextClassLoader();
			c2.getResource(urlStr);
			System.out.println("u2.....");
			Enumeration<URL> u2 = c2.getResources(urlStr);
			iterator(u2);
			//..............................................................
			c2.getResource(urlStr);
			System.out.println("u3.....");
			Enumeration<URL> u3 = c2.getResources("");
			iterator(u3);	
			System.out.println("u4.....");
			URL u4 = TestClassLoaderTwo.class.getResource(urlStr);
			System.out.println(u4.getPath());
			
//			ClassLoader c = TestClassLoaderTwo.class.;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void iterator(Enumeration<URL> urls) {
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			System.out.println(url.getPath());
		}
	}
	

}
