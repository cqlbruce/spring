package net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketBase {
	
	public static void main(String[] args) {
		ServerSocket ss = null ; 
		try {
			ss = new ServerSocket(8080);
			while(true) {
				Socket s = ss.accept() ; 
				OutputStream os = s.getOutputStream();
				System.out.println("coming...");
				os.write("thanks you!".getBytes());
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
