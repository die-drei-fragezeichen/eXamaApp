package ch.diedreifragezeichen.exama.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	//whatever is in adminTemplets is visible to admins
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/adminTemplates/userCreate").setViewName("users/create");
		registry.addViewController("/adminTemplates/usersShow").setViewName("users/show");
		registry.addViewController("/adminTemplates/subjectCreate").setViewName("subjects/create");
		registry.addViewController("/adminTemplates/subjectsShow").setViewName("subjects/show");

		registry.addViewController("/403").setViewName("403");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

}