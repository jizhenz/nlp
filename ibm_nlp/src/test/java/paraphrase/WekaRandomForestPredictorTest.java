package paraphrase;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;

public class WekaRandomForestPredictorTest {
	
	private String sentence_1 = "What is your age ?";
	private String sentence_2 = "How old are you ?";

	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		Util.init();
		WekaRandomForestPredictor.init();
	}

	@Test
	public void test() {
		WekaRandomForestPredictor wrf;
		try {
			wrf = new WekaRandomForestPredictor();
			System.out.println(wrf.predict(sentence_1, sentence_2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
