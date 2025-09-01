package app.students.Service_S;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ServiceSApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSApplication.class, args);
	}

}
