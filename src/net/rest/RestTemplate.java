package net.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;

public class RestTemplate {
	
	
	private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

	
	public <T> T postForObject(String url , Object request , Class<T> responseType , Object... uriVariables) throws RestClientException{
		RequestCallback requestCallback = httpEntityCallback(request , responseType);
		
		return null ; 
	}
	
	
	
	
	
	public List<HttpMessageConverter<?>> getMessageConverters() {
		return this.messageConverters;
	}
	
	protected <T> RequestCallback httpEntityCallback(Object requestBody,Class<T> responseType) {
		//Object requestBody , Type responseType, List<HttpMessageConverter<?>> messageConverters
		return new HttpEntityRequestCallback(requestBody , responseType , messageConverters);
	}

}
