package paraphrase;

import java.io.IOException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class FeatureGeneratorTest {
	
	private String[] sentence_1 = {"What", "is", "your", "age", "?"};
	private String[] sentence_2 = {"What", "is", "your", "weight", "?"};
	private String[] sentence_3 = {"How", "old", "are", "you", "?"};

	@BeforeClass
	public static void oneTimeSetUp() throws IOException {
	}
	
	@Test
	public void testGet_edit_distance() {
		System.out.println("edit_distance: " + FeatureGenerator.get_edit_distance(sentence_1, sentence_1));
		System.out.println("edit_distance: " + FeatureGenerator.get_edit_distance(sentence_1, sentence_2));
		System.out.println("edit_distance: " + FeatureGenerator.get_edit_distance(sentence_1, sentence_3));
	}

	@Test
	public void testGet_jw_distance() {
		System.out.println("jw_distance: " + FeatureGenerator.get_jw_distance(sentence_1, sentence_1));
		System.out.println("jw_distance: " + FeatureGenerator.get_jw_distance(sentence_1, sentence_2));
		System.out.println("jw_distance: " + FeatureGenerator.get_jw_distance(sentence_1, sentence_3));
	}

	@Test
	public void testGet_onehot_vector() {
		Object[] m = FeatureGenerator.get_onehot_vector(sentence_1, sentence_2);
		Map<String,Integer> x = (Map<String,Integer>)(m[0]);
		Map<String,Integer> y = (Map<String,Integer>)(m[1]);
		for (String key : x.keySet()) {
			System.out.print("("+key+","+x.get(key)+"),");
		}
		System.out.println();
		for (String key : y.keySet()) {
			System.out.print("("+key+","+y.get(key)+"),");
		}
		System.out.println();
	}

	@Test
	public void testGet_manhattan_distance() {
		System.out.println("manhattan_distance: " + FeatureGenerator.get_manhattan_distance(sentence_1, sentence_2));
	}

	@Test
	public void testGet_euclidean_distance() {
		System.out.println("euclidean_distance: " + FeatureGenerator.get_euclidean_distance(sentence_1, sentence_2));
	}

	@Test
	public void testGet_cosine_distance() {
		System.out.println("cosine_distance: " + FeatureGenerator.get_cosine_distance(sentence_1, sentence_2));
	}

//	@Test
//	public void testGet_ngram_distance() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGet_matching_coefficient() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGet_dice_coefficient() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGet_jaccard_coefficient() {
//		fail("Not yet implemented");
//	}

}
