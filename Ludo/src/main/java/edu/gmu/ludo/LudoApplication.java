package edu.gmu.ludo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@ImportResource({"classpath:/spring/applicationContext.xml","classpath:/spring/hibernateContext.xml","classpath:/spring/securityContext.xml"})
public class LudoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LudoApplication.class, args);
	}

}
