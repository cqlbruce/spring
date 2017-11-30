package resttemplate.single;


public class TestPost {
	
	public static void main(String[] args) {
		System.out.println("start...");
		String url = "http://127.0.0.1:8080";
		RestTemplate rt = new RestTemplate();
		
		Object recStr = (Object)rt.postForObject(url,"hello",Object.class);
		
		System.out.println(recStr.toString());
		
	}
	

}
