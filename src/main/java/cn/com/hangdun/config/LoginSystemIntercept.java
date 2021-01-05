package cn.com.hangdun.config;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginSystemIntercept implements HandlerInterceptor {
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	        try {
	            //统一拦截（查询当前session是否存在user）(这里user会在每次登陆成功后，写入session)
	            Object user=request.getSession().getAttribute("es_user");
	            System.out.println(user);
	            if(user!=null){
	                return true;
	            }else {
	            	Cookie[] cookies = request.getCookies();
					 if (cookies != null) {
					    for (Cookie cookie2 : cookies) {
					        // 若登录，放行
					        if ("loginName".equals(cookie2.getName())) {
					        	request.setAttribute("es_user", cookie2.getName());
					            System.out.println("Cookie："+cookie2.getName());
					            return true;
					        }
					    }
					 }
	            	response.sendRedirect("/es/login");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return false;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
	                     //如果设置为true时，请求将会继续执行后面的操作
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
	
	
	
}
