package resizing.imageresizing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ImageResizingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageResizingApplication.class, args);
	}

}
