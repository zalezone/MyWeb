package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import dao.UserDao;
import domain.User;
import exception.UserExistException;

@Service
public class UserService {
    
    @Autowired
    private UserDao userDao;
    
    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }
    public User getUserById(int userId) {
        return userDao.get(userId);
    }

    //注册一个新用户，如果用户名已经存在则抛出UserExistException异常
    public void register(User user)throws UserExistException{
        User u = this.getUserByUserName(user.getUserName());
        if (u!=null) {
            throw new UserExistException("用户名已经存在");
        }
        else {
            userDao.save(user);
        }
    }

}