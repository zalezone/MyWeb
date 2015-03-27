package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.User;
import exception.UserExistException;
import service.UserService;

@Controller
public class RegisterController extends BaseController{
    @Autowired
    private UserService userService;
    //用户注册
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request,User user)
    {
        ModelAndView view = new ModelAndView();
        view.setViewName("/success");
        try {
            userService.register(user);
        } catch (UserExistException e) {
            view.addObject("errorMsg", "用户名已经存在，请选择其他名字");
            view.setViewName("forward:/register.jsp");
        }
        setSessionUser(request, user);
        return view;   
    }
    
    @RequestMapping(value = "/reg")
    public String reg()
    {
        return "register";
    }
}
