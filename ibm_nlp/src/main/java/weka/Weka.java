package weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

public class Weka {

	/** the classifier used internally */
	protected Classifier m_Classifier = null;

	/** the filter to use */
	protected Filter m_Filter = null;

	/** the training file */
	protected String m_TrainingFile = null;

	/** the training instances */
	protected Instances m_Training = null;

	/** for evaluating the classifier */
	protected Evaluation m_Evaluation = null;

	public Weka() {
	}

	/**
	 * sets the classifier to use
	 * 
	 * @param name
	 *            the classname of the classifier
	 * @param options
	 *            the options for the classifier
	 */
	public void setClassifier(String name, String[] options) throws Exception {
		m_Classifier = AbstractClassifier.forName(name, options);
	}

	/**
	 * sets the filter to use
	 * 
	 * @param name
	 *            the classname of the filter
	 * @param options
	 *            the options for the filter
	 */
	public void setFilter(String name, String[] options) throws Exception {
		m_Filter = (Filter) Class.forName(name).newInstance();
		if (m_Filter instanceof OptionHandler)
			((OptionHandler) m_Filter).setOptions(options);
	}

	/**
	 * outputs some data about the classifier
	 */
	public String toString() {
		StringBuffer result;

		result = new StringBuffer();
		result.append("Weka - for NLP \n===========\n\n");

		if (null != m_Classifier) {
			result.append("Classifier...: " + m_Classifier.getClass().getName());
			if (m_Classifier instanceof OptionHandler) {
				result.append(" " + Utils.joinOptions(((OptionHandler) m_Classifier).getOptions()));
			}
			result.append("\n");
			result.append(m_Classifier.toString() + "\n");
		}
		
		if (null != m_Filter) {
			if (m_Filter instanceof OptionHandler)
				result.append("Filter.......: " + m_Filter.getClass().getName() + " "
						+ Utils.joinOptions(((OptionHandler) m_Filter).getOptions()) + "\n");
			else
				result.append("Filter.......: " + m_Filter.getClass().getName() + "\n");
		}

		if (null != m_TrainingFile) {
			result.append("Training file: " + m_TrainingFile + "\n");
			result.append("\n");
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
