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
		registry.addViewController("/adminTemplates/editUser").setViewName("users/edit");
		registry.addViewController("/adminTemplates/userSaved").setViewName("users/saved");
		registry.addViewController("/adminTemplates/showUsers").setViewName("users/show");
		registry.addViewController("/adminTemplates/showSubjects").setViewName("subjects/show");
		registry.addViewController("/adminTemplates/editSubject").setViewName("subjects/edit");

		registry.addViewController("/403").setViewName("403");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

}