package paraphrase;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;
import weka.core.Instance;
import weka.core.Instances;

public class WekaParaphraseDataLoaderTest {

	@BeforeClass
	public static void oneTimeSetUp() throws IOException, ClassNotFoundException {
		Util.init();
	}
	
	@Test
	public void test() {
		WekaParaphraseDataLoader loader;
		try {
			loader = new WekaParaphraseDataLoader();
			Instances is = loader.loadArff(Util.getCurrentPath() + "/weka/SVM.C10-fea.130.arff", -1);
			printInstances(is);
			is = null;
			is = loader.loadArffIncrementally(Util.getCurrentPath() + "/weka/SVM.C10-fea.130.arff", -1);
			printInstances(is);
			is = null;
			is = loader.load(Util.getCurrentPath() + "/weka/msr/msr_paraphrase_train.txt", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void printInstances(Instances is) {
		int cnt = 0, upper = Math.min(5, is.numInstances());
		System.out.println(is.numAttributes() + " " + is.numInstances());
		Instance current;
		while (cnt < upper) {
			current = is.get(cnt);
			System.out.println(current.toString());
			if (cnt++>upper) break;
		}
	}

}
