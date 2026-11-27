package tn.esprit.avis.avis.config;

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
    public OpenAPI avisOpenApi(@Value("${server.port:8085}") String port) {
        return new OpenAPI()
                .info(new Info()
                        .title("Avis Microservice API")
                        .version("1.0.0")
                        .description("API REST pour la gestion des avis, notes, réservations éligibles et signalements (MarahbeBik).")
                        .contact(new Contact().name("Maryem Samaali")))
                .servers(List.of(new Server().url("http://localhost:" + port).description("Local")));
    }
}