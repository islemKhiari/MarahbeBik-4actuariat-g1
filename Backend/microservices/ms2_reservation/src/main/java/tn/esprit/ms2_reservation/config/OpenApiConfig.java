package tn.esprit.ms2_reservation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservationOpenApi(@Value("${server.port:8083}") String port) {
        return new OpenAPI()
                .info(new Info()
                        .title("Réservation Microservice API")
                        .version("1.0.0")
                        .description("REST API pour gérer les réservations de séjour, les invités, "
                                + "les blocs de disponibilité des chambres et les politiques d'annulation."))
                .servers(List.of(new Server().url("http://localhost:" + port).description("Local")));
    }
}
