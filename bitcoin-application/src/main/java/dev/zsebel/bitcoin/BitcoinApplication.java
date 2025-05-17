package dev.zsebel.bitcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BitcoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinApplication.class, args);
	}
}
