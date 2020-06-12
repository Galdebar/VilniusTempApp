package lt.galdebar.vilniustemp;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Log4j2
public class VilniusTempApplication {

	public static void main(String[] args) {
		SpringApplication.run(VilniusTempApplication.class, args);
		log.info("Vilnius Temp App up and running");
	}

}
