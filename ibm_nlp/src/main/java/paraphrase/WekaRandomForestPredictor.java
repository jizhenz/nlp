package paraphrase;

import weka.core.Instance;
import weka.classifiers.trees.RandomForest;
import weka.filters.unsupervised.instance.Randomize;

public class WekaRandomForestPredictor {

	private static Weka wekaSingle;
	public static void init() throws Exception {
		String[] options = { "-I", "15", "-K", "30", "-S", "1" };
		String name = "weka.classifiers.trees.RandomForest";
		String filterName = "weka.filters.unsupervised.instance.Randomize";
		String[] filterOptions = { "-S", "42" };
		String dir = "/weka/msr/";
		String trainName = "msr_paraphrase_train";
		String testName = "msr_paraphrase_test";
		int classIndex = 0;
		wekaSingle = Weka.getSingleton(name, options, filterName, filterOptions, dir, trainName, testName, classIndex);
	}

	public WekaRandomForestPredictor(){
	}

	public boolean predict(String sentence_1, String sentence_2) throws Exception {
		boolean isPara = false;
		FeatureGenerator fg = null;
		try {
			fg = new FeatureGenerator();
			Instance instance = fg.generateInstance(wekaSingle.m_TrainingSet, sentence_1, sentence_2);
			double[] probs = wekaSingle.classify(instance);
			isPara = (probs[0] < probs[1]) ? true : false;
		} catch (Exception e) {
			throw e;
		}
		return isPara;
	}

}
