package net.server;

import org.springframework.web.client.RestTemplate;

public class TestPost {
	
	public static void main(String[] args) {
		
		String url = "http://127.0.0.1:8080";
		RestTemplate rt = new RestTemplate();
		
		String recStr = (String)rt.postForObject(url,"hello",String.class);
		
		System.out.println(recStr);
		
	}
	
}
