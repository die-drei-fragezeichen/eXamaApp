package ch.diedreifragezeichen.exama._config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
// import org.thymeleaf.spring5.ISpringTemplateEngine;
// import org.thymeleaf.spring5.SpringTemplateEngine;
// import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/*
	 * @Override public void addViewControllers(ViewControllerRegistry registry) {
	 * registry.addViewController("/login").setViewName("login");
	 * registry.addViewController("/adminTemplates/userCreate").setViewName(
	 * "users/create");
	 * registry.addViewController("/adminTemplates/usersShow").setViewName(
	 * "users/show");
	 * registry.addViewController("/adminTemplates/subjectCreate").setViewName(
	 * "subjects/create");
	 * registry.addViewController("/adminTemplates/subjectsShow").setViewName(
	 * "subjects/show"); registry.addViewController("/403").setViewName("403");
	 * registry.setOrder(Ordered.HIGHEST_PRECEDENCE); }
	 */
}