package wordnet;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class WordnetClientTest {

	@Test
	public void testGetSynonyms() {
		WordnetClient wc;
		try {
			wc = WordnetClient.getWordnetClient();
			List<Map<String,String>> lst = wc.getSynonyms("dog");
			for (Map<String,String> m : lst) {
				System.out.println(m.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
