package paraphrase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaRandomForestPredictorTest {

	@BeforeClass
	public static void oneTimeSetUp() throws IOException, ClassNotFoundException {
		Util.init();
	}

	@Test
	public void test() {
		WekaRandomForestPredictor wrf;
		try {
			wrf = new WekaRandomForestPredictor();
			wrf.predict("", "");
			
			ArffLoader loader = new ArffLoader();
			loader.setFile(new File("C:/Users/jizhen/Documents/GitHub/paraphrase-recognition/results/SVM.C10-fea.130.arff"));
			//Instances isStruct = loader.getStructure();
			Instances isData   = loader.getDataSet();
			Instance current;
			int upper = 10;
			int cnt = 0;
//			System.out.println(isStruct.numAttributes() + " " + isStruct.numInstances());
//			while ((current = loader.getNextInstance(isStruct)) != null) {
//				System.out.println(current.toString());
//				if (cnt++>upper) break;
//			}
			
			cnt = 0;
			System.out.println(isData.numAttributes() + " " + isData.numInstances());
			while (cnt < upper) {
				current = isData.get(cnt);
				System.out.println(current.toString());
				if (cnt++>upper) break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
