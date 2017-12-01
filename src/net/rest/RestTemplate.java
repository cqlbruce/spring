package net.rest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.InterceptingHttpAccessor;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.DefaultUriTemplateHandler;

public class RestTemplate extends InterceptingHttpAccessor implements RestOperations {
	protected final Log logger = LogFactory.getLog(getClass());
	private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
	
	private static boolean romePresent =
			ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", RestTemplate.class.getClassLoader());
	
	private static final boolean jackson2XmlPresent =
			ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", RestTemplate.class.getClassLoader());


	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader()) &&
					ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader());

	private static final boolean gsonPresent =
			ClassUtils.isPresent("com.google.gson.Gson", RestTemplate.class.getClassLoader());
	
	private static final boolean jaxb2Present =
			ClassUtils.isPresent("javax.xml.bind.Binder", RestTemplate.class.getClassLoader());

	
	public RestTemplate() {
		this.messageConverters.add(new ByteArrayHttpMessageConverter());
		this.messageConverters.add(new StringHttpMessageConverter());
		this.messageConverters.add(new ResourceHttpMessageConverter());
		this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
		
		if (romePresent) {
			this.messageConverters.add(new AtomFeedHttpMessageConverter());
			this.messageConverters.add(new RssChannelHttpMessageConverter());
		}

		if (jackson2XmlPresent) {
			this.messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
		}
		else if (jaxb2Present) {
			this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		}

//		if (jackson2Present) {
//			this.messageConverters.add(new MappingJackson2HttpMessageConverter());
//		}
		else if (gsonPresent) {
			this.messageConverters.add(new GsonHttpMessageConverter());
		}
	
	}
	
	
	@Override
	public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		RequestCallback requestCallback = httpEntityCallback(request, responseType);
		HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(),logger);
		return (T) execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
	}

	@Override
	public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException {
		URI expanded = new DefaultUriTemplateHandler().expand(url , uriVariables);
		return doExecute(expanded, method, requestCallback, responseExtractor);
	}	
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		ClientHttpResponse response = null;
		try {
			ClientHttpRequest request = createRequest(url, method);
			if (requestCallback != null) {
				//干嘛用呢？
				//组装请求头信息  同时 开始往outputStream写信息并且flush
				requestCallback.doWithRequest(request);
			}
			//干嘛用的呢？
			//这里才开始write outputStream ?
			response = request.execute();
			//处理server响应码
			handleResponse(url, method, response);
			if (responseExtractor != null) {
				//根据返回responseType转型。
				return responseExtractor.extractData(response);
			}
			else {
				return null;
			}
		}
		catch (IOException ex) {
			String resource = url.toString();
			String query = url.getRawQuery();
			resource = (query != null ? resource.substring(0, resource.indexOf(query) - 1) : resource);
			throw new ResourceAccessException("I/O error on " + method.name() +
					" request for \"" + resource + "\": " + ex.getMessage(), ex);
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
	}
	
	protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException{
		ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
		boolean hasError = errorHandler.hasError(response);
		if (hasError) {
			errorHandler.handleError(response);
		}
	}
	
	public List<HttpMessageConverter<?>> getMessageConverters() {
		return this.messageConverters;
	}
	
	protected <T> RequestCallback httpEntityCallback(Object requestBody,Class<T> responseType) {
		//Object requestBody , Type responseType, List<HttpMessageConverter<?>> messageConverters
		return new HttpEntityRequestCallback(requestBody , responseType , messageConverters);
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public HttpHeaders headForHeaders(String url, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public HttpHeaders headForHeaders(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public URI postForLocation(String url, Object request, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public URI postForLocation(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public URI postForLocation(URI url, Object request) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
			Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public void put(String url, Object request, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void put(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void put(URI url, Object request) throws RestClientException {
		// TODO Auto-generated method stub
		
	}






	@Override
	public <T> T patchForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T patchForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T patchForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public void delete(String url, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void delete(String url, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void delete(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		
	}






	@Override
	public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
