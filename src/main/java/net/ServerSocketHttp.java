package net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketHttp {
    //服务器根目录，post.html, upload.html都放在该位置
    public static String WEB_ROOT = "c:/root";
    //端口
    private static int port = 8080;
    //用户请求的文件的url
    private static String requestPath;
    //mltipart/form-data方式提交post的分隔符, 
    private static String boundary = null;
    //post提交请求的正文的长度
    private static int contentLength = 0;
	
	
	public static void main(String[] args) {
        try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("server is ok.");
			//开启serverSocket等待用户请求到来，然后根据请求的类别作处理
			//在这里我只针对GET和POST作了处理
			//其中POST具有解析单个附件的能力
			while (true) {
			    System.out.println("new request coming begin...");
			    Socket socket = serverSocket.accept();
			    System.out.println("new request coming.");
			    DataInputStream reader = new DataInputStream((socket.getInputStream()));
			    String line = reader.readLine();
			    String method = line.substring(0, 4).trim();
			    OutputStream out = socket.getOutputStream();
			    requestPath = line.split(" ")[1];
			    System.out.println(method);
			    if ("GET".equalsIgnoreCase(method)) {
			        System.out.println("do get......");
//                doGet(reader, out);
			    } else if ("POST".equalsIgnoreCase(method)) {
			        System.out.println("do post......");
			        doPost(reader, out);
			    }
			    socket.close();
			    System.out.println("socket closed.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    //处理post请求
    private static void doPost(DataInputStream reader, OutputStream out) throws Exception {
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
            if ("".equals(line)) {
                break;
            } else if (line.indexOf("Content-Length") != -1) {
                contentLength = Integer.parseInt(line.substring(line.indexOf("Content-Length") + 16));
            }
            //表明要上传附件， 跳转到doMultiPart方法。
//            else if(line.indexOf("multipart/form-data")!= -1){
//                //得multiltipart的分隔符
//                this.boundary = line.substring(line.indexOf("boundary") + 9);
//                this.doMultiPart(reader, out);
//                return;
//            }
        }
        //继续读取普通post（没有附件）提交的数据
        System.out.println("begin reading posted data......");
        String dataLine = null;
        //用户发送的post数据正文
        byte[] buf = {};
        int size = 0;
        if (contentLength != 0) {
            buf = new byte[contentLength];
            while(size<contentLength){
                int c = reader.read();
                buf[size++] = (byte)c;
                 
            }
            System.out.println("The data user posted: " + new String(buf, 0, size));
        }
        //发送回浏览器的内容
        String response = "";
        response += "HTTP/1.1 200 OK\n";
        response += "Server: Sunpache 1.0\n";
        response += "Content-Type: text/html\n";
        response += "Last-Modified: Mon, 11 Jan 1998 13:23:42 GMT\n";
        response += "Accept-ranges: bytes";
        response += "\n";
        
        
//        String body = "<html><head><title>test server</title></head><body><p>post ok:</p>" + new String(buf, 0, size) + "</body></html>";
        String body = "thanks for you visitor ..." ;  
        PrintWriter pw = new PrintWriter(out);
        
        System.out.println(body);
//        out.write(response.getBytes());
//        out.write(body.getBytes());
//        out.flush();
        pw.println(response);
        pw.println(body);
        pw.flush();
        reader.close();
        out.close();
        System.out.println("request complete.");
    }

}
