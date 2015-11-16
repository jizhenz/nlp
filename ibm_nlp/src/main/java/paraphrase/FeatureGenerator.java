package paraphrase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class FeatureGenerator {

	private static final int BIG_NUMBER = 1_000_000;

	/**
	 * Code in
	 * https://github.com/wead-hsu/paraphrase-recognition/blob/master/src/
	 * Feature_Generator.py is not write. Also based on:
	 * https://en.wikipedia.org/wiki/Edit_distance
	 * http://stackoverflow.com/questions/5055839/word-level-edit-distance-of-a-
	 * sentence
	 * 
	 * Uses: Wagner–Fischer algorithm - Dynamic Algorithm
	 * 
	 * Time: theta(mn) Space: theta(mn). linear space version: Hirschberg's
	 * algorithm
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return int
	 */
	public static int get_edit_distance(String[] sentence_1, String[] sentence_2) {
		int len_1 = sentence_1.length;
		int len_2 = sentence_2.length;

		if (0 == len_1 || 0 == len_2) {
			return Math.max(len_1, len_2);
		}

		int dp[][] = new int[len_1 + 1][len_2 + 1];

		for (int i = 0; i <= len_1; i++) {
			for (int j = 0; j <= len_2; j++) {
				dp[i][j] = BIG_NUMBER;
			}
		}
		dp[0][0] = 0;
		for (int i = 1; i <= len_1; ++i)
			dp[i][0] = i;
		for (int i = 1; i <= len_2; ++i)
			dp[0][i] = i;

		for (int i = 1; i <= len_1; i++) {
			for (int j = 1; j <= len_2; j++) {
				dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
						dp[i - 1][j - 1] + (sentence_1[i - 1].equals(sentence_2[j - 1]) ? 0 : 1));
			}
		}
		return dp[len_1][len_2];
	}

	/**
	 * Jaro-Winkler distance
	 * 
	 * https://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance
	 * https://github.com/wead-hsu/paraphrase-recognition/blob/master/src/
	 * Feature_Generator.py
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return
	 */
	public static double get_jw_distance(String[] sentence_1, String[] sentence_2) {
		String sen_1 = String.join("", sentence_1);
		String sen_2 = String.join("", sentence_2);

		int len_1 = sen_1.length();
		int len_2 = sen_2.length();

		int m = 0;
		int len_max = Math.max(len_1, len_2);
		int match_scope = len_max / 2 - 1;

		int match_line_1[] = new int[len_1];
		int match_line_2[] = new int[len_2];

		for (int i = 0; i < len_1; i++) {
			for (int j = Math.max(0, i - match_scope); j < Math.min(len_2, i + match_scope); j++) {
				if (0 == match_line_2[j] && sen_1.charAt(i) == sen_2.charAt(j)) {
					match_line_1[i] = 1;
					match_line_2[j] = 1;
					m += 1;
					break;
				}
			}
		}

		if (0 == m) {
			return 0;
		}

		List<Character> result_line_1 = new ArrayList<Character>();
		List<Character> result_line_2 = new ArrayList<Character>();

		for (int i = 0; i < len_1; i++) {
			if (match_line_1[i] > 0) {
				result_line_1.add(sen_1.charAt(i));
			}
		}

		for (int i = 0; i < len_2; i++) {
			if (match_line_2[i] > 0) {
				result_line_2.add(sen_2.charAt(i));
			}
		}

		int t = 0;
		int result_len = result_line_1.size();
		for (int i = 0; i < result_len; i++) {
			if (!result_line_1.get(i).equals(result_line_2.get(i))) {
				t += 1;
			}
		}

		int l = 0;
		while (l < sen_1.length() && l < sen_2.length() && sen_1.charAt(l) == sen_2.charAt(l)) {
			l += 1;
		}

		double dj = m / 3.0 / len_1 + m / 3.0 / len_2 + (m - t) / 3.0 / m;
		double dw = dj + l * 0.1 * (1 - dj);

		return dw;
	}

	public static Object[] get_onehot_vector(String[] sentence_1, String[] sentence_2) {
		Set<String> words = new HashSet<String>();

		for (String w : sentence_1) {
			words.add(w);
		}
		for (String w : sentence_2) {
			words.add(w);
		}

		Map<String, Integer> x = new HashMap<String, Integer>();
		Map<String, Integer> y = new HashMap<String, Integer>();

		for (String w : words) {
			x.put(w, 0);
			y.put(w, 0);
		}

		for (String w : sentence_1) {
			x.put(w, x.get(w) + 1);
		}
		for (String w : sentence_2) {
			y.put(w, y.get(w) + 1);
		}

		Object[] m = new Object[2];
		m[0] = (Object) x;
		m[1] = (Object) y;
		return m;
	}

	/**
	 * https://en.wikipedia.org/wiki/Taxicab_geometry
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return int
	 */
	public static int get_manhattan_distance(String[] sentence_1, String[] sentence_2) {
		Object[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String, Integer> x = (Map<String, Integer>) (m[0]);
		Map<String, Integer> y = (Map<String, Integer>) (m[1]);

		int dis = 0;
		for (String w : x.keySet()) {
			dis += Math.max(x.get(w) - y.get(w), y.get(w) - x.get(w));
		}

		return dis;
	}

	/**
	 * https://en.wikipedia.org/wiki/Euclidean_distance
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return double
	 */
	public static double get_euclidean_distance(String[] sentence_1, String[] sentence_2) {
		Object[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String, Integer> x = (Map<String, Integer>) (m[0]);
		Map<String, Integer> y = (Map<String, Integer>) (m[1]);

		int dis = 0;
		for (String w : x.keySet()) {
			dis += (x.get(w) - y.get(w)) * (x.get(w) - y.get(w));
		}

		return Math.sqrt(dis);
	}

	/**
	 * https://en.wikipedia.org/wiki/Cosine_similarity
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return
	 */
	public static double get_cosine_distance(String[] sentence_1, String[] sentence_2) {
		Object[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String, Integer> x = (Map<String, Integer>) (m[0]);
		Map<String, Integer> y = (Map<String, Integer>) (m[1]);

		int len_x = 0;
		int len_y = 0;
		int xy = 0;

		for (String w : x.keySet()) {
			len_x += x.get(w) * x.get(w);
			len_y += y.get(w) * y.get(w);
			xy += x.get(w) * y.get(w);
		}

		if (0 == len_x || 0 == len_y) {
			return 0.0;
		}
		return xy / Math.sqrt(len_x) / Math.sqrt(len_y);
	}

	/**
	 * Uses 3-gram
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return int
	 */
	public static int get_ngram_distance(String[] sentence_1, String[] sentence_2) {
		int len_1 = sentence_1.length;
		int len_2 = sentence_2.length;
		if (len_1 > 2 && len_2 > 2) {
			String[] mod_sentence_1 = new String[len_1 - 2];
			String[] mod_sentence_2 = new String[len_2 - 2];

			for (int i = 0; i < len_1 - 2; i++) {
				mod_sentence_1[i] = String.join(" ", sentence_1[i], sentence_1[i + 1], sentence_1[i + 2]);
			}

			for (int i = 0; i < len_2 - 2; i++) {
				mod_sentence_2[i] = String.join(" ", sentence_2[i], sentence_2[i + 1], sentence_2[i + 2]);
			}

			return get_manhattan_distance(mod_sentence_1, mod_sentence_2);
		}
		return 0;
	}

	/**
	 * https://en.wikipedia.org/wiki/Simple_matching_coefficient
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return
	 */
	public static int get_matching_coefficient(String[] sentence_1, String[] sentence_2) {
		Object[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String, Integer> x = (Map<String, Integer>) (m[0]);
		Map<String, Integer> y = (Map<String, Integer>) (m[1]);

		int cnt_xy = 0;
		for (String w : x.keySet()) {
			if (null != x.get(w) && 0 != x.get(w) && null != y.get(w) && 0 != y.get(w)) {
				cnt_xy += 1;
			}
		}
		return cnt_xy;
	}

	/**
	 * https://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return
	 */
	public static double get_dice_coefficient(String[] sentence_1, String[] sentence_2) {
		Object[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String, Integer> x = (Map<String, Integer>) (m[0]);
		Map<String, Integer> y = (Map<String, Integer>) (m[1]);

		int cnt_xy = 0;
		int cnt_x = 0;
		int cnt_y = 0;
		for (String w : x.keySet()) {
			if (null != x.get(w) && 0 != x.get(w)) {
				cnt_x += 1;
			}
			if (null != y.get(w) && 0 != y.get(w)) {
				cnt_y += 1;
			}
			if (null != x.get(w) && 0 != x.get(w) && null != y.get(w) && 0 != y.get(w)) {
				cnt_xy += 1;
			}
		}

		if (0 == cnt_x + cnt_y) {
			return 0.0;
		}

		return 2.0 * cnt_xy / (cnt_x + cnt_y);
	}

	/**
	 * Count(A and B)/Count(A or B) https://en.wikipedia.org/wiki/Jaccard_index
	 * 
	 * @param sentence_1
	 * @param sentence_2
	 * @return
	 */
	public static double get_jaccard_coefficient(String[] sentence_1, String[] sentence_2) {
		Object[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String, Integer> x = (Map<String, Integer>) (m[0]);
		Map<String, Integer> y = (Map<String, Integer>) (m[1]);

		if (x.keySet().isEmpty()) {
			return 0;
		}

		int cnt_xy = 0;
		int cnt_x_or_y = 0;
		for (String w : x.keySet()) {
			if (null != x.get(w) && 0 != x.get(w) && null != y.get(w) && 0 != y.get(w)) {
				cnt_xy += 1;
			}
			cnt_x_or_y += 1;
		}
		for (String w : y.keySet()) {
			if (!x.containsKey(w)) {
				cnt_x_or_y += 1;
			}
		}
		return 1.0 * cnt_xy / cnt_x_or_y;
	}

	private SortedMap<DATA, Boolean> dataOption;
	private SortedMap<FEATURE, Boolean> featureOption;

	public FeatureGenerator() {
		dataOption = new TreeMap<DATA, Boolean>();
		for (DATA data : DATA.values()) {
			dataOption.put(data, true);
		}
		featureOption = new TreeMap<FEATURE, Boolean>();
		for (FEATURE data : FEATURE.values()) {
			featureOption.put(data, true);
		}
	}

	public StringBuffer generateArffHeader(String relationName) {
		StringBuffer sb = new StringBuffer();
		sb.append("@relation " + relationName + "\n");
		sb.append("@attribute answer {0,1}\n");
		for (DATA d : this.dataOption.keySet()) {
			if (!this.dataOption.get(d)) {
				continue;
			}
			for (FEATURE f : this.featureOption.keySet()) {
				if (!this.featureOption.get(f)) {
					continue;
				}
				sb.append("@attribute " + d.name() + "." + f.name() + " NUMERIC\n");
			}
		}
		sb.append("@data\n");
		return sb;
	}

	public double[] generateArffDataRow(String sentence_1, String sentence_2) throws IOException {
		double[] values = new double[this.getNumTotalFeatures()];
		Sentence s1 = new Sentence(sentence_1);
		Sentence s2 = new Sentence(sentence_2);
		int inx = 0;
		for (DATA d : this.dataOption.keySet()) {
			if (!this.dataOption.get(d)) {
				continue;
			}
			for (FEATURE f : this.featureOption.keySet()) {
				if (!this.featureOption.get(f)) {
					continue;
				}
				double v = this.getFeatureValue(s1, s2, d, f);
				values[inx++] = v;
			}
		}
		return values;
	}
	
	public Instance generateInstance(Instances template, String sentence_1, String sentence_2) throws IOException {
		Instance instance = new DenseInstance(template.firstInstance());
		instance.setDataset(template);
		Sentence s1 = new Sentence(sentence_1);
		Sentence s2 = new Sentence(sentence_2);
		int inx=0;
		instance.setValue(inx++, 0);
		double v = 0;
		for (DATA d : this.dataOption.keySet()) {
			if (!this.dataOption.get(d)) {
				continue;
			}
			for (FEATURE f : this.featureOption.keySet()) {
				if (!this.featureOption.get(f)) {
					continue;
				}
				v = this.getFeatureValue(s1, s2, d, f);
				instance.setValue(inx++, v);
			}
		}
		return instance;
	}

	private double getFeatureValue(Sentence s1, Sentence s2, DATA d, FEATURE f) {
		double v = 0;

		String[] seq_1 = {};
		String[] seq_2 = {};
		switch (d) {
		case Original:
			seq_1 = s1.getOrigTokens();
			seq_2 = s2.getOrigTokens();
			break;
		case Stem:
			seq_1 = s1.getStems();
			seq_2 = s2.getStems();
			break;
		case POS:
			seq_1 = s1.getTags();
			seq_2 = s2.getTags();
			break;
		case Soundex:
			seq_1 = s1.getSoundex();
			seq_2 = s2.getSoundex();
			break;
		case Nouns:
			seq_1 = s1.getNouns();
			seq_2 = s2.getNouns();
			break;
		case NounStem:
			seq_1 = s1.getNoun_stems();
			seq_2 = s2.getNoun_stems();
			break;
		case NounSoundex:
			seq_1 = s1.getNoun_soundex();
			seq_2 = s2.getNoun_soundex();
			break;
		case Verbs:
			seq_1 = s1.getVerbs();
			seq_2 = s2.getVerbs();
			break;
		case VerbStem:
			seq_1 = s1.getVerb_stems();
			seq_2 = s2.getVerb_stems();
			break;
		case VerbSoundex:
			seq_1 = s1.getVerb_soundex();
			seq_2 = s2.getVerb_soundex();
			break;
		default:
			break;
		}

		switch (f) {
		case Edit:
			v = FeatureGenerator.get_edit_distance(seq_1, seq_2);
			break;
		case JW:
			v = FeatureGenerator.get_jw_distance(seq_1, seq_2);
			break;
		case Manhattan:
			v = FeatureGenerator.get_manhattan_distance(seq_1, seq_2);
			break;
		case Euclidean:
			v = FeatureGenerator.get_euclidean_distance(seq_1, seq_2);
			break;
		case Cosine:
			v = FeatureGenerator.get_cosine_distance(seq_1, seq_2);
			break;
		case NGram:
			v = FeatureGenerator.get_ngram_distance(seq_1, seq_2);
			break;
		case Matching:
			v = FeatureGenerator.get_matching_coefficient(seq_1, seq_2);
			break;
		case Dice:
			v = FeatureGenerator.get_dice_coefficient(seq_1, seq_2);
			break;
		case Jaccard:
			v = FeatureGenerator.get_jaccard_coefficient(seq_1, seq_2);
			break;
		default:
			break;
		}

		//System.out.println(d.name() + "." + f.name() + ": " + v);

		return v;
	}

	public int getNumTotalFeatures() {
		return this.getNumDataOption() * this.getNumFeatureOption();
	}

	public Boolean update(DATA key, boolean value) {
		return this.dataOption.replace(key, value);
	}

	public Boolean update(FEATURE key, boolean value) {
		return this.featureOption.replace(key, value);
	}

	private int getNumDataOption() {
		int n = 0;
		for (boolean b : this.dataOption.values()) {
			if (b)
				n++;
		}
		return n;
	}

	private int getNumFeatureOption() {
		int n = 0;
		for (boolean b : this.featureOption.values()) {
			if (b)
				n++;
		}
		return n;
	}
}
