package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import wordnet.WordnetClient;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
			WordnetClient.getWordnetClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
