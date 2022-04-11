package de.neusta.ncc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class NccApplication {

    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplication(NccApplication.class);
        springApplication.run(args);
    }

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(
                new Info().title("Neusta code challenge REST services")
                        .version("0.0.1")
                        .contact(new Contact().name("Lars Michaelis").email("l.michaelis@neusta.de"))
        );
    }

}
