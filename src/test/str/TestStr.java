package test.str;

import org.junit.Test;

public class TestStr {

	@Test
	public void test() {
		String str = "Content-Length: 13";
		System.out.println(str.indexOf("Content-Lengh:"));
		String str2 = "abc";
		System.out.println(str2.indexOf("a"));
		
	}

}
