package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paraphrase.WekaRandomForestPredictor;
import util.Util;
import wordnet.WordnetClient;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
        	Util.init();
        	WordnetClient.getWordnetClient();
        	WekaRandomForestPredictor.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
