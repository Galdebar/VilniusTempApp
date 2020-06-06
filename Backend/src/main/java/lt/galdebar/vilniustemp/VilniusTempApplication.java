package lt.galdebar.vilniustemp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VilniusTempApplication {

	public static void main(String[] args) {
		SpringApplication.run(VilniusTempApplication.class, args);
		System.out.println("App Runs");
	}

}
