package world.moducare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ModucareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModucareApplication.class, args);
	}

}
