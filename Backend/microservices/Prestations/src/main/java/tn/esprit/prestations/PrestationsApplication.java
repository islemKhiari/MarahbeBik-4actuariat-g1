
package tn.esprit.prestations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PrestationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrestationsApplication.class, args);
    }
}
