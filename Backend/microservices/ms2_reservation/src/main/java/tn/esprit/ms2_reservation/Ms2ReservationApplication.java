package tn.esprit.ms2_reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Ms2ReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ms2ReservationApplication.class, args);
    }

}
