package test.mvc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class TestSocket {

	@Test
	public void test() {
		try {
			Socket s = new Socket("127.0.0.1", 8080);
			InputStream is = s.getInputStream() ;
			byte[] b = new byte[1024];
			is.read(b);
			System.out.println(new String(b));
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}
