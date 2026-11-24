package tn.esprit.ms1_hebergement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Ms1HebergementApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ms1HebergementApplication.class, args);
    }

}
