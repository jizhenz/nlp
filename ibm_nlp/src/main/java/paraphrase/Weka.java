package paraphrase;

import java.io.File;
import java.io.IOException;

import util.Util;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.filters.unsupervised.instance.Randomize;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

public class Weka {

	protected FilteredClassifier m_FC = null;
	protected Classifier m_Classifier = null;
	protected Filter m_Filter = null;
	protected Instances m_TrainingSet = null;
	protected Instances m_TestingSet = null;
	protected Evaluation m_Evaluation = null;
	
	static private Weka weka = null;
	
	static public Weka getSingleton(String name, String[] options, String filterName, 
			String[] filterOptions, String dir, String trainName, String testName, int classIndex) 
				throws Exception {
		if (weka == null) {
			weka = new Weka(name, options, filterName, filterOptions, dir, trainName, testName, classIndex);
		}
		return weka;
	}

	private Weka(String name, String[] options, String filterName, String[] filterOptions, String dir, String trainName, String testName, int classIndex) throws Exception {
		setClassifier(name, options);
		setFilter(filterName, filterOptions);
		setTrainingData(dir, trainName, classIndex);
		setTestingData(dir, testName, classIndex);
		build();
	}

	public void setClassifier(String name, String[] options) throws Exception {
		m_Classifier = (Classifier)Class.forName(name).newInstance(); //(RandomForest.forName(name, options));
		if (m_Filter instanceof OptionHandler)
			((OptionHandler) m_Filter).setOptions(options);
	}

	public void setFilter(String name, String[] options) throws Exception {
		m_Filter = (Filter) Class.forName(name).newInstance();
		if (m_Filter instanceof OptionHandler)
			((OptionHandler) m_Filter).setOptions(options);
	}
	
	public void setTrainingData(String dir, String trainName, int classIndex) throws IOException {
		WekaParaphraseDataLoader loader = new WekaParaphraseDataLoader();
		String base = Util.getCurrentPath() + dir + trainName;
		String txtFile = base+".txt";
		String arffFile = base+".arff";
		File fArff = new File(arffFile);
		if (fArff.exists()) {
			this.m_TrainingSet = loader.loadArff(arffFile, classIndex);
		} else {
			this.m_TrainingSet = loader.load("train", txtFile, arffFile, classIndex);
		}
	}
	
	public void setTestingData(String dir, String testName, int classIndex) throws IOException {
		WekaParaphraseDataLoader loader = new WekaParaphraseDataLoader();
		String base = Util.getCurrentPath() + dir + testName;
		String txtFile = base+".txt";
		String arffFile = base+".arff";
		File fArff = new File(arffFile);
		if (fArff.exists()) {
			this.m_TestingSet = loader.loadArff(arffFile, classIndex);
		} else {
			this.m_TestingSet = loader.load("test", txtFile, arffFile, classIndex);
		}
	}
	
	public void build() throws Exception {
		m_FC = new FilteredClassifier();
		m_FC.setFilter(this.m_Filter);
		m_FC.setClassifier(this.m_Classifier);
		m_FC.buildClassifier(m_TrainingSet);
		m_Evaluation = new Evaluation(m_TrainingSet);
		m_Evaluation.evaluateModel(m_FC, m_TestingSet);
	}
	
	public String getModelSummary() {
		return m_Evaluation.toSummaryString();
	}
	
	public double[] classify(Instance instance) throws Exception {
		instance.setDataset(m_TrainingSet);
		return m_FC.distributionForInstance(instance);
	}

	public String toString() {
		StringBuffer result;

		result = new StringBuffer();
		result.append("Weka - for NLP \n===========\n");

		if (null != m_Classifier) {
			result.append("Classifier...: " + m_Classifier.getClass().getName());
			if (m_Classifier instanceof OptionHandler) {
				result.append(" " + Utils.joinOptions(((OptionHandler) m_Classifier).getOptions()));
			}
			result.append("\n");
			result.append(m_Classifier.toString());
		}
		
		if (null != m_Filter) {
			if (m_Filter instanceof OptionHandler)
				result.append("Filter.......: " + m_Filter.getClass().getName() + " "
						+ Utils.joinOptions(((OptionHandler) m_Filter).getOptions()) + "\n");
			else
				result.append("Filter.......: " + m_Filter.getClass().getName() + "\n");
		}

		if (null != m_Evaluation) {
			result.append(m_Evaluation.toSummaryString() + "\n");
			try {
				result.append(m_Evaluation.toMatrixString() + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				result.append(m_Evaluation.toClassDetailsString() + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result.toString();
	}

}
