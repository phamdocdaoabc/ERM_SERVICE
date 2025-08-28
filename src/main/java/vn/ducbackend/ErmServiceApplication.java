package vn.ducbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "vn.ducbackend.client")
public class ErmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErmServiceApplication.class, args);
	}

}
