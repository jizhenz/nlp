package paraphrase;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import wordnet.WordnetClient;

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
