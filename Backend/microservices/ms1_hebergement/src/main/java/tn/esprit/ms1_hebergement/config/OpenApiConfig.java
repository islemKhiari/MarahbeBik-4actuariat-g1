package tn.esprit.ms1_hebergement.config;

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
    public OpenAPI hebergementOpenApi(@Value("${server.port:8081}") String port) {
        return new OpenAPI()
                .info(new Info()
                        .title("Hébergement Microservice API")
                        .version("1.0.0")
                        .description("REST API pour gérer les maisons d'hôtes, leurs chambres "
                                + "(avec prix par nuit) et les équipements proposés.")
                        .contact(new Contact().name("islem khiari")))
                .servers(List.of(new Server().url("http://localhost:" + port).description("Local")));
    }
    //commentaire
}