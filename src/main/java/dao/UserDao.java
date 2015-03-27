package dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sun.org.apache.bcel.internal.generic.DADD;

import domain.User;

@Repository
public class UserDao extends BaseDao<User> {

    private final String GET_USER_BY_USERNAME = "from User u where u.userName = ?";

	 public User getUserByUserName(String userName){
	        List<User> users = (List<User>)find(GET_USER_BY_USERNAME,userName);
	        if (users.size() == 0) {
	            return null;
	        }else{
	            return users.get(0);
	        }
	    }
}
