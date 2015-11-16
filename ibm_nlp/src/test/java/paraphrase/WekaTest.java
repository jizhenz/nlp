package paraphrase;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;

public class WekaTest {
	
	@BeforeClass
	public static void oneTimeSetUp() throws IOException, ClassNotFoundException {
		Util.init();
	}
	
	@Test
	public void test() {
		String[] options = {"-I", "15", "-K", "30", "-S", "1"};
		String name = "weka.classifiers.trees.RandomForest";
		String filterName = "weka.filters.unsupervised.instance.Randomize";
		String[] filterOptions = {"-S", "42"};
		String dir = "/weka/msr/";
		String trainBase = "msr_paraphrase_train";
		String testBase = "msr_paraphrase_test";
		Weka weka = null; 
		try {
			weka = Weka.getSingleton(name, options, filterName, filterOptions, dir, trainBase, testBase, 0);
			System.out.println(weka.toString());
			System.out.println("===========================================================================");
			String strSummary = weka.getModelSummary();
			System.out.println(strSummary);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

}
