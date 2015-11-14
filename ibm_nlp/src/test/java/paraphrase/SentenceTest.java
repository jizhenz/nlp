package paraphrase;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;

public class SentenceTest {

	@BeforeClass
	public static void oneTimeSetUp() throws IOException {
		Util.init();
	}

	@Test
	public void test() {
		Sentence sen;
		try {
			sen = new Sentence("How old are you successfully?");
			sen.print();
			sen = new Sentence("This is a fat clever pig?");
			sen.print();
			System.out.println(sen.soundex("how"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
