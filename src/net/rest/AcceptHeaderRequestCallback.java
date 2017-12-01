package net.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RequestCallback;

public class AcceptHeaderRequestCallback implements RequestCallback{
	
	private final Type responseType ; 
	private List<HttpMessageConverter<?>> messageConverters = null ; 
	
	public AcceptHeaderRequestCallback(Type responseType , List<HttpMessageConverter<?>> messageConverters) {
		this.responseType = responseType;
		this.messageConverters = messageConverters ; 
	}
	
	@Override
	public void doWithRequest(ClientHttpRequest request) throws IOException {
		
		if(this.responseType != null) {
			Class<?> responseClass = null;
			if(this.responseType instanceof Class) {
				responseClass = (Class<?>) this.responseType;
			}
			//当前支持的MediaTypes
			List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>(); 
			for(HttpMessageConverter<?> converter : getMessageConverters()) {
				if(responseClass != null) {
					if(converter.canRead(responseClass, null)) {
						allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
					}
				}else if (converter instanceof GenericHttpMessageConverter) {
					GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
					if (genericConverter.canRead(this.responseType, null, null)) {
						allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
					}
				}
				
				if (!allSupportedMediaTypes.isEmpty()) {
					MediaType.sortBySpecificity(allSupportedMediaTypes);
//					if (logger.isDebugEnabled()) {
//						logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
//					}
					// 给ClientHttpRequest 设置 Content-Type 可解析的报文格式 json text/html 
					//这样服务端必须返回此种类型格式 否则可能解析失败。 
					request.getHeaders().setAccept(allSupportedMediaTypes);
				}
			}
		}
	}
	
	private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
		List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
		List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
		for (MediaType supportedMediaType : supportedMediaTypes) {
			if (supportedMediaType.getCharset() != null) {
				supportedMediaType =
						new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
			}
			result.add(supportedMediaType);
		}
		return result;
	}
	
	
	
	public List<HttpMessageConverter<?>> getMessageConverters() {
		return this.messageConverters;
	}
	
}
