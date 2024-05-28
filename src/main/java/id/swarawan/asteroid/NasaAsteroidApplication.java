package id.swarawan.asteroid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NasaAsteroidApplication {

	public static void main(String[] args) {
		SpringApplication.run(NasaAsteroidApplication.class, args);
	}

}
