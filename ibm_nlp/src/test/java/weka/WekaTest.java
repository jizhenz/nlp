package weka;

import org.junit.Test;

public class WekaTest {

	@Test
	public void test() {
		Weka weka = new Weka();
		try {
			String name = "weka.classifiers.trees.RandomForest";
			String [] options = {"-I", "15", "-K", "91"};
			weka.setClassifier(name, options);
			System.out.println(weka.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

}
