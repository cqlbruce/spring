package test.mvc.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;

public class RestTemplate {
	
	
	private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

	
	
	
	public List<HttpMessageConverter<?>> getMessageConverters() {
		return this.messageConverters;
	}

}
