package controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cons.CommonConstant;
import domain.User;
import service.UserService;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
    
    @Autowired
    private UserService userService;
    
    //用户登陆
    @RequestMapping("/doLogin")
    public ModelAndView login(HttpServletRequest request,User user)
    {
        User dbUser = userService.getUserByUserName(user.getUserName());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("foward:/login.jsp");
        if (dbUser == null) {
            mav.addObject("errorMsg","用户名不存在");
        }
        else if(!dbUser.getPassword().equals(user.getPassword())){
            mav.addObject("errorMsg", "用户名密码不正确");
        }
        else{
            dbUser.setLastIp(request.getRemoteAddr());
            dbUser.setLastVisit(new Date());
            setSessionUser(request, dbUser);
            String toUrl = (String)request.getSession().getAttribute(CommonConstant.LOGIN_TO_URL);
            request.getSession().removeAttribute(CommonConstant.LOGIN_TO_URL);
            //如果当前会话中没有保存登录之前的请求URL，则直接跳转到主页
            if (StringUtils.isEmpty(toUrl)) {
                toUrl ="index.html";
            }
            mav.setViewName("redirect:"+toUrl);
        }
        return mav;
    }
    
    //登陆注销
    @RequestMapping("/doLogout")
    public String logout(HttpSession session)
    {
        session.removeAttribute(CommonConstant.USER_CONTEXT);
        return "forward:/index.jsp";
    }
}
