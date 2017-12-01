package test.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class JdkHttpRequest {
	
	public static String sendGet(String url , String param ) {
		String result = "";
		BufferedReader in = null ; 
		try {
			String urlNameString = url + "?" + param ; 
			URL realUrl = new URL(urlNameString);
			//建立连接连接对象
			URLConnection connection = realUrl.openConnection();
			//设置http请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible ; MSIE 6.0 ; "
					+ "Windows NT 5.1 ; SV1)");
			//建立连接
			connection.connect();
			
			//获取所有响应头字段
			Map<String , List<String>> map = connection.getHeaderFields();
			for(String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String line ;
			while((line = in.readLine()) != null) {
				result += line ; 
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(in != null) {
					in.close();
				}
			}catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return result ; 
	}
	
	public static String sendPost(String url , String param) {
		PrintWriter out = null ; 
		BufferedReader in = null ; 
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			// httpUrlConnection.setDoOutput(true);以后就可以使用conn.getOutputStream().write()  
			conn.setDoOutput(true);
			//httpUrlConnection.setDoInput(true);以后就可以使用conn.getInputStream().read(); 
			conn.setDoInput(true);
//			conn.connect();
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			InputStream is = conn.getInputStream() ;
			byte[] b = new byte[1024];
			is.read(b);
			System.out.println(new String(b));
//			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line ;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
		}catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
            //使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}    
	
	public static void main(String[] args) {
		String s = JdkHttpRequest.sendGet("http://localhost:8080/Home/RequestString", "key=123&v=456");
		System.out.println(s);
//        String sr=JdkHttpRequest.sendPost("http://localhost:8080/Home/RequestPostString", "key=123&v=456");
//        System.out.println(sr);
	}

}
