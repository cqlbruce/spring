package net.rest;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class TestTemplate {

	@Test
	public void test() {
		try {
			
			String url = "http://127.0.0.1:8080";
			RestTemplate rt = new RestTemplate();
			
			String recStr = (String)rt.postForObject(url,"hello",String.class);
			
//			JSONObject recStr = (JSONObject)rt.postForObject(url,"hello",JSONObject.class);
			
			
			System.out.println(recStr.toString());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		
		String url = "http://127.0.0.1:8080";
		org.springframework.web.client.RestTemplate rt = new org.springframework.web.client.RestTemplate();
		String recStr = (String)rt.postForObject(url,"hello",String.class);
		System.out.println(recStr);
		
	}
	

}
