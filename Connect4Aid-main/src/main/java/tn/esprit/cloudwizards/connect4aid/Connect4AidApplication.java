package tn.esprit.cloudwizards.connect4aid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Connect4AidApplication {

    public static void main(String[] args) {
        SpringApplication.run(Connect4AidApplication.class, args);
    }

}
