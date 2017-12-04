package net.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import net.rest.bean.Person;

public class TestTemplate {

	@Test
	public void test() {
		try {
			
//			String url = "http://127.0.0.1:8080";
//			String url = "http://127.0.0.1:9080/hello";
			String url = "http://127.0.0.1:9080/test";

			RestTemplate rt = new RestTemplate();
			
//			String recStr = (String)rt.postForObject(url,"hello",String.class);
			
//			JSONObject recStr = (JSONObject)rt.postForObject(url,"hello",JSONObject.class);
			Person p = new Person();
			p.setBody("good");
			p.setHead("big");
			p.setLeg("long");
			Map<String , String> map = new HashMap<String,String>(); 
			map.put("str", "hello");

			Person recStr = (Person)rt.postForObject(url,"hello",Person.class);
			System.out.println(recStr.toString());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		
		String url = "http://127.0.0.1:9080/{str}";
		org.springframework.web.client.RestTemplate rt = new org.springframework.web.client.RestTemplate();
		Map<String,String> map = new HashMap<String,String>();
		map.put("str", "hello");
		String recStr = (String)rt.postForObject(url,map,String.class,map);
		System.out.println(recStr);
		
	}
	
	@Test
	public void test3(){
		
		UriComponents uriComponents =UriComponentsBuilder.newInstance()  
			       .scheme("http").host("example.com").path("/hotels/{hotel}/bookings/{booking}").build()  
			       .expand("42", "21")  
			       .encode(); 
		System.out.println(uriComponents.toString());
//		URI expanded = getUriTemplateHandler().expand(url, uriVariables);
//		DefaultUriTemplateHandler
//		System.out.println(uriComponents);
		
	}
	

}
