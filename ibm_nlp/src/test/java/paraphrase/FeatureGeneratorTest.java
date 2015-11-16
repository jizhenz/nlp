package paraphrase;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;

public class FeatureGeneratorTest {
	
	private String[] sentence_1 = {"What", "is", "your", "age", "?"};
	private String[] sentence_2 = {"What", "is", "your", "weight", "?"};
	private String[] sentence_3 = {"How", "old", "are", "you", "?"};
	private FeatureGenerator fg = null;

	@BeforeClass
	public static void oneTimeSetUp() throws IOException, ClassNotFoundException {
		Util.init();
	}
	
    @Before
    public void setUp() {
    	fg = new FeatureGenerator();
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

	@Test
	public void testGet_ngram_distance() {
		System.out.println("ngram_distance: " + FeatureGenerator.get_ngram_distance(sentence_1, sentence_2));
	}

	@Test
	public void testGet_matching_coefficient() {
		System.out.println("matching_coefficient: " + FeatureGenerator.get_matching_coefficient(sentence_1, sentence_2));
	}

	@Test
	public void testGet_dice_coefficient() {
		System.out.println("dice_coefficient: " + FeatureGenerator.get_dice_coefficient(sentence_1, sentence_2));
	}

	@Test
	public void testGet_jaccard_coefficient() {
		System.out.println("jaccard_coefficient: " + FeatureGenerator.get_jaccard_coefficient(sentence_1, sentence_2));
	}
	
	@Test
	public void test() {
		System.out.println(fg.generateArffHeader("train").toString());
		try {
			String str1 = String.join(" ", sentence_1);
			String str2 = String.join(" ", sentence_2);
			System.out.println(str1 + "\n" + str2 + "\n");
			System.out.println(Arrays.toString(fg.generateArffDataRow(str1, str2)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
