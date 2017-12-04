package spring.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.mvc.domain.User;
import net.mvc.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestUserService {

	@Autowired
	private UserService userService;
	
	@Test
	public void hasMatchUser() {
		boolean b1 = userService.hasMatchUser("admin", "123456");
		boolean b2 = userService.hasMatchUser("admin", "1111");
		System.out.println(b1);
		System.out.println(b2);
//		assert(b1);
//		assertTrue(b1);
//		assertTrue(b2);
	}
	
	@Test
	public void findUserByUserName(){
		User user = userService.findUserByUserName("admin");
		System.out.println(user.getUserName());
//		assertE
//		assert(user.getUserId() , "admin");
	}

}
