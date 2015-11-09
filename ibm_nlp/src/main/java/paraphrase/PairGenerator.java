package paraphrase;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class PairGenerator {
	
	private final Stemmer stemmer;
	
	public PairGenerator() {
		stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
	}

}
