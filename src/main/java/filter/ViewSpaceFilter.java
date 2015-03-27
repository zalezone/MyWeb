package filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import cons.CommonConstant;
import domain.User;

public class ViewSpaceFilter {
    
    private static final String FILTERED_REQUEST = "@@session_context_filtered_request";
    
    //不需要登陆就能访问的URI资源
    private static final String[] INHERENT_ESCAPE_URIS = 
        {"index.jsp","index.html","login.jsp","login/doLogin.html","register.jsp","register.html"};
    
    //执行过滤
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
            throws IOException,ServletException
    {
        //保证该过滤器在一次请求中只被调用一次
        if (request!=null&&request.getAttribute(FILTERED_REQUEST)!=null) {
            chain.doFilter(request, response);
        }
        else 
        {
            //设置过滤标识，防止一次请求多次过滤
            request.setAttribute(FILTERED_REQUEST, Boolean.TRUE);
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            User userContext = getSessionUser(httpRequest);
            
            //用户未登录，并且当前的URL需要登录才能访问
            if(userContext == null&&!isURILogin(httpRequest.getRequestURI(), httpRequest))
            {
                String toUrl = httpRequest.getRequestURL().toString();
                if (!StringUtils.isEmpty(httpRequest.getQueryString())) {
                    toUrl += "?" + httpRequest.getQueryString();
                }
                
                //将用户的请求放在session中，用于登录成功后，跳转到目标URL
                httpRequest.getSession().setAttribute(CommonConstant.LOGIN_TO_URL, toUrl);
                
                //转发到登录页面
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            chain.doFilter(request, response);
        }
    }
    
    private boolean isURILogin(String requestURI,HttpServletRequest request)
    {
        if (request.getContextPath().equalsIgnoreCase(requestURI)
                ||(request.getContextPath()+"/").equalsIgnoreCase(requestURI)) {
            return true;
        }
        for(String uri:INHERENT_ESCAPE_URIS)
        {
            if (requestURI!=null&&requestURI.indexOf(uri)>=0) {
                return true;
            }
        }
        return false;
    }
    
    protected User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(CommonConstant.USER_CONTEXT);
    }
}
