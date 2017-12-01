package net.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

public class HttpEntityRequestCallback extends AcceptHeaderRequestCallback{
	
	private final HttpEntity<?> requestEntity ; 
	

	public HttpEntityRequestCallback(Object requestBody , Type responseType, List<HttpMessageConverter<?>> messageConverters) {
		super(responseType, messageConverters);
		if(requestBody instanceof HttpEntity){
			this.requestEntity = (HttpEntity<?>)requestBody ; 
		} else if (requestBody != null){
			this.requestEntity = new HttpEntity<Object>(requestBody);
		} else {
			this.requestEntity = HttpEntity.EMPTY;
		}
	}
	
	public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
		//设置accept type 
		super.doWithRequest(httpRequest);
		//
		if(!this.requestEntity.hasBody()){
			HttpHeaders httpHeaders = httpRequest.getHeaders();
			HttpHeaders requestHeaders = this.requestEntity.getHeaders();
			if(!requestHeaders.isEmpty()){
				httpHeaders.putAll(httpHeaders);
			}
			if (httpHeaders.getContentLength() < 0) {
				httpHeaders.setContentLength(0L);
			}
		}else{
			Object requestBody = this.requestEntity.getBody();
			Class<?> requestBodyClass = requestBody.getClass();
			Type requestBodyType = (this.requestEntity instanceof RequestEntity ? ((RequestEntity<?>)this.requestEntity).getType():requestBodyClass);
			HttpHeaders requestHeaders = this.requestEntity.getHeaders();
			MediaType requestContentType = requestHeaders.getContentType();
			for (HttpMessageConverter<?> messageConverter : getMessageConverters()) {
				if (messageConverter instanceof GenericHttpMessageConverter) {
					GenericHttpMessageConverter<Object> genericMessageConverter = (GenericHttpMessageConverter<Object>) messageConverter;
					if (genericMessageConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
						if (!requestHeaders.isEmpty()) {
							//组装相关http 头信息
							httpRequest.getHeaders().putAll(requestHeaders);
						}
						genericMessageConverter.write(requestBody, requestBodyType, requestContentType, httpRequest);
						return;
					}
				} else if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
					if (!requestHeaders.isEmpty()) {
						//组装相关http 头信息
						httpRequest.getHeaders().putAll(requestHeaders);
					}
					//开始往outputStream写信息并且flush
					((HttpMessageConverter<Object>) messageConverter).write(
							requestBody, requestContentType, httpRequest);
					return;
				}
			}
		}
	}
}
