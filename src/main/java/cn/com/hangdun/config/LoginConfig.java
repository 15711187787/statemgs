package cn.com.hangdun.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		InterceptorRegistration addInterceptor = registry.addInterceptor(new LoginSystemIntercept());
		ArrayList<String> exclude = new ArrayList<>();
		exclude.add("/login");
		exclude.add("/");
		exclude.add("/index");
		exclude.add("/getSystemStatus");
		exclude.add("/sendMyStateMgs");
		exclude.add("/linkLogin");
		exclude.add("/loginJumpToDo");
		exclude.add("/loginJump");
		exclude.add("/ssaLogin");
		exclude.add("/js/**");
		exclude.add("/css/**");
		exclude.add("/image/**");
		exclude.add("/fonts/**");
		addInterceptor.addPathPatterns("/**").excludePathPatterns(exclude);
	}
}