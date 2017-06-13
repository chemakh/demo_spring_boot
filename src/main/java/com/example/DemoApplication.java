package com.example;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RestController
	public class TestController {

		@RequestMapping(value = "/", method = RequestMethod.GET)
		public String sayHello() {
			return "Hello from spring boot";
		}

	}
	
	@Bean
	public TomcatEmbeddedServletContainerFactory containerFactory() {
	    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
	     factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	        @Override
	        public void customize(Connector connector) {
	         ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
	        }
	     });
	     return factory;
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/auth").allowedOrigins("*");
				registry.addMapping("/files/**").allowedOrigins("*");
				registry.addMapping("/stomp/**").allowedOrigins("*");
				registry.addMapping("/addUser").allowedOrigins("*");
				registry.addMapping("/addTags").allowedOrigins("*");
				registry.addMapping("/addNews").allowedOrigins("*");
				registry.addMapping("/news").allowedOrigins("*");
				registry.addMapping("/tags").allowedOrigins("*");
				registry.addMapping("/user").allowedOrigins("*");
			}
		};
	}
}
