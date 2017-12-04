package net.mvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import net.mvc.domain.User;

@Repository
public class UserDao {

	
	@Autowired
	private JdbcTemplate jdbcTemplate ; 
	
	
	public int getMatchConut(String userName , String password){
		
		String sqlStr = "SELECT count(*) FROM t_user WHERE user_name = ? and password = ?" ;
//		String sqlStr = "SELECT * FROM t_user WHERE user_name = ? and password = ?" ;
		List<Integer> userList = jdbcTemplate.queryForList(sqlStr, new Object[]{userName , password} , Integer.class);
		
		if(CollectionUtils.isEmpty(userList)){
			return 0 ; 
		}
		 return userList.size();
	}
	
	
	public User findUserByUserName(final String userName ){
		String sqlStr = "SELECT user_id , user_name , credits FROM t_user WHERE user_name = ?";
		final User user = new User();
		jdbcTemplate.query(sqlStr, new Object[]{userName}, new RowCallbackHandler(){
			public void processRow(ResultSet rs)throws SQLException{
				user.setUserId(rs.getInt("user_id"));
				user.setUserName(userName);
				user.setCredits(rs.getInt("credits"));
			}
		});
		return user;
	}
	
	public void updateLoginInfo(User user){
		String sqlStr = "UQDATE t_user SET last_visit = ? , last_ip = ? , credits = ? WHERE user_id = ?";
		jdbcTemplate.update(sqlStr , new Object[]{user.getLastVisit() , user.getLastIp() , user.getCredits() , user.getUserId()});
	}
	
	
}
