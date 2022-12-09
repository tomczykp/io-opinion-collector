package pl.lodz.p.it.opinioncollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OpinionCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpinionCollectorApplication.class, args);
    }
}
