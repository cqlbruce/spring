package net.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

public class MessageBodyClientHttpResponseWrapper  implements ClientHttpResponse {

	
	private final ClientHttpResponse response;

	private PushbackInputStream pushbackInputStream;


	public MessageBodyClientHttpResponseWrapper(ClientHttpResponse response) throws IOException {
		this.response = response;
	}
	
	
	public boolean hasMessageBody() throws IOException {
		try {
			HttpStatus responseStatus = getStatusCode();
			if (responseStatus.is1xxInformational() || responseStatus == HttpStatus.NO_CONTENT ||
					responseStatus == HttpStatus.NOT_MODIFIED) {
				return false;
			}
		}
		catch (IllegalArgumentException ex) {
			// Ignore - unknown HTTP status code...
		}
		if (getHeaders().getContentLength() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean hasEmptyMessageBody() throws IOException {
		InputStream body = this.response.getBody();
		if (body == null) {
			return true;
		}
		else if (body.markSupported()) {
			body.mark(1);
			if (body.read() == -1) {
				return true;
			}
			else {
				body.reset();
				return false;
			}
		}
		else {
			this.pushbackInputStream = new PushbackInputStream(body);
			int b = this.pushbackInputStream.read();
			if (b == -1) {
				return true;
			}
			else {
				this.pushbackInputStream.unread(b);
				return false;
			}
		}
	}
	
	@Override
	public HttpStatus getStatusCode() throws IOException {
		return this.response.getStatusCode();
	}
	
	
	@Override
	public HttpHeaders getHeaders() {
		return this.response.getHeaders();
	}
	
	
	
	@Override
	public InputStream getBody() throws IOException {
		return (this.pushbackInputStream != null ? this.pushbackInputStream : this.response.getBody());
	}	
	

	@Override
	public int getRawStatusCode() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStatusText() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	
	
}
