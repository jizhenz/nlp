package paraphrase;

import org.junit.Test;

public class WekaRandomForestTest {

	@Test
	public void test() {
		WekaRandomForest wrf;
		try {
			wrf = new WekaRandomForest();
			wrf.getTagged("How old are you?");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
