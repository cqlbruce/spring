package net.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

public class HttpMessageConverterExtractor<T> implements ResponseExtractor{
	private final Type responseType;
	private final Class<T> responseClass;
	private final List<HttpMessageConverter<?>> messageConverters;
	private final Log logger;

	
	
	public HttpMessageConverterExtractor(Type responseType , 
			List<HttpMessageConverter<?>> messageConverters , Log logger) {
		this.responseType = responseType;
		this.responseClass = (responseType instanceof Class ? (Class<T>) responseType : null);
		this.messageConverters = messageConverters;
		this.logger = logger;
	}
	

	@Override
	@SuppressWarnings({"unchecked", "rawtypes", "resource"})
	public T extractData(ClientHttpResponse response) throws IOException {
		MessageBodyClientHttpResponseWrapper responseWrapper = new MessageBodyClientHttpResponseWrapper(response);
		if (!responseWrapper.hasMessageBody() || responseWrapper.hasEmptyMessageBody()) {
			return null;
		}
		MediaType contentType = getContentType(responseWrapper);

		for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
			if (messageConverter instanceof GenericHttpMessageConverter) {
				GenericHttpMessageConverter<?> genericMessageConverter =
						(GenericHttpMessageConverter<?>) messageConverter;
				if (genericMessageConverter.canRead(this.responseType, null, contentType)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Reading [" + this.responseType + "] as \"" +
								contentType + "\" using [" + messageConverter + "]");
					}
					return (T) genericMessageConverter.read(this.responseType, null, responseWrapper);
				}
			}
			if (this.responseClass != null) {
				if (messageConverter.canRead(this.responseClass, contentType)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Reading [" + this.responseClass.getName() + "] as \"" +
								contentType + "\" using [" + messageConverter + "]");
					}
					return (T) messageConverter.read((Class) this.responseClass, responseWrapper);
				}
			}
		}

		throw new RestClientException("Could not extract response: no suitable HttpMessageConverter found " +
				"for response type [" + this.responseType + "] and content type [" + contentType + "]");
	}
	
	
	private MediaType getContentType(ClientHttpResponse response) {
		MediaType contentType = response.getHeaders().getContentType();
		if (contentType == null) {
			if (logger.isTraceEnabled()) {
				logger.trace("No Content-Type header found, defaulting to application/octet-stream");
			}
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}
		return contentType;
	}
}
