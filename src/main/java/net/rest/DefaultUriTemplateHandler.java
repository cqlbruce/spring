package net.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.web.util.AbstractUriTemplateHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

public class DefaultUriTemplateHandler extends AbstractUriTemplateHandler{

	private boolean parsePath;

	private boolean strictEncoding;
	
	
	
	@Override
	protected URI expandInternal(String uriTemplate, Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected URI expandInternal(String uriTemplate, Object... uriVariables) {
		UriComponentsBuilder uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
		UriComponents uriComponents = expandAndEncode(uriComponentsBuilder, uriVariables);

		
		
		return null;
	}
	
	protected UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
		if (shouldParsePath() && !isStrictEncoding()) {
			List<String> pathSegments = builder.build().getPathSegments();
			builder.replacePath(null);
			for (String pathSegment : pathSegments) {
				builder.pathSegment(pathSegment);
			}
		}
		return builder;
		
	}
	
	protected UriComponents expandAndEncode(UriComponentsBuilder builder, Object[] uriVariables) {
		if (!isStrictEncoding()) {
			return builder.buildAndExpand(uriVariables).encode();
		}
		else {
			Object[] encodedUriVars = new Object[uriVariables.length];
			for (int i = 0; i < uriVariables.length; i++) {
				encodedUriVars[i] = applyStrictEncoding(uriVariables[i]);
			}
			return builder.buildAndExpand(encodedUriVars);
		}
	}
	
	private String applyStrictEncoding(Object value) {
		String stringValue = (value != null ? value.toString() : "");
		try {
			return UriUtils.encode(stringValue, "UTF-8");
		}
		catch (UnsupportedEncodingException ex) {
			// Should never happen
			throw new IllegalStateException("Failed to encode URI variable", ex);
		}
	}
	
	public boolean shouldParsePath() {
		return this.parsePath;
	}
	
	public boolean isStrictEncoding() {
		return this.strictEncoding;
	}
	
}
