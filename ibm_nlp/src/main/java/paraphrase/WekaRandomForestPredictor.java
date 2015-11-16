package paraphrase;

import java.io.IOException;

import util.Util;
import weka.classifiers.trees.RandomForest;

public class WekaRandomForestPredictor {

	private RandomForest rf;

	public WekaRandomForestPredictor() throws IOException, ClassNotFoundException {
		this.rf = Util.getRf();
	}

	public boolean predict(String sentence_1, String sentence_2) throws IOException {
		boolean isPara = false;
		
		Sentence s1=null, s2=null;
		IOException ioe = null;
		
		try {
			int nf = rf.getNumFeatures();
			s1 = new Sentence(sentence_1);
			s2 = new Sentence(sentence_2);
			
		} catch (IOException e) {
			ioe = e;
			throw ioe;
		} finally {
			
		}
		
		return isPara;
	}

}
