package exrf.pos;

import exrf.pos.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class PosApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(PosApplication.class);
		application.addInitializers(applicationContext -> {
					ConfigurableEnvironment environment = applicationContext.getEnvironment();
					DotenvConfig.loadFromRootDir(environment);
				}
		);
		application.run(args);

	}

}
