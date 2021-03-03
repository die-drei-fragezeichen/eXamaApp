package ch.diedreifragezeichen.exama._config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
// import org.thymeleaf.spring5.ISpringTemplateEngine;
// import org.thymeleaf.spring5.SpringTemplateEngine;
// import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	// whatever is in adminTemplets is visible to admins
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

	// private ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
	// 	SpringTemplateEngine engine = new SpringTemplateEngine();
	// 	engine.addDialect(new Java8TimeDialect());
	// 	engine.setTemplateResolver(templateResolver);
	// 	return engine;
	// }


}