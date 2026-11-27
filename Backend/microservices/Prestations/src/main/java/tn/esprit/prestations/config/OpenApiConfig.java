package tn.esprit.prestations.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI prestationOpenApi(
            @Value("${server.port:8083}") String port) {

        return new OpenAPI()
                .info(new Info()
                        .title("Prestations Microservice API")
                        .version("1.0.0")
                        .description(
                                "REST API to manage prestations "
                                        + "and their reservations "
                                        + "(bidirectional one-to-many relationship).")
                        .contact(new Contact()
                                .name("Equipe MarahbeBik")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + port)
                                .description("Local")));
    }
}