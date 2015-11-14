package paraphrase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FeatureGenerator {

	public static int get_edit_distance(String[] sentence_1, String[] sentence_2){
		int len_1 = sentence_1.length;
		int len_2 = sentence_2.length;

		if (0==len_1 || 0==len_2) {
			return Math.max(len_1, len_2);
		}

		int dp[][] = new int[len_1][len_2];
		
		for (int i=0; i<len_1; i++) {
			for (int j=0; j<len_2; j++) {
				dp[i][j] = 1000;
			}
		}
		dp[0][0] = 0;
		for (int i=0; i<len_1; i++) {
			for (int j=0; j<len_2; j++) {
				if (i > 0) {
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j] + 1);
				}
				if (j > 0) {
					dp[i][j] = Math.min(dp[i][j], dp[i][j-1] + 1);
				}
				if (sentence_1[i].equals(sentence_2[j])){
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j-1]);
				}
				else {
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j-1] + 1);
				}
			}
		}
		return dp[len_1-1][len_2-1];
	}
	
	public static double get_jw_distance(String[] sentence_1, String[] sentence_2){
		String sen_1 = String.join("", sentence_1);
		String sen_2 = String.join("", sentence_2);
			
		int len_1 = sen_1.length();
		int len_2 = sen_2.length();

		int m = 0;
		int len_max = Math.max(len_1, len_2);
		int match_scope = len_max/2 - 1;
		
		int match_line_1[] = new int[len_1];
		int match_line_2[] = new int[len_2];

		for (int i=0; i<len_1; i++) {
			for (int j=Math.max(0, i-match_scope); j<Math.min(len_2, i+match_scope); j++){
				if (0==match_line_2[j] && sen_1.charAt(i)==sen_2.charAt(j)) {
					match_line_1[i] = 1;
					match_line_2[j] = 1;
					m += 1;
					break;
				}
			}
		}
		
		if (0 ==m) {
			return 0;
		}

		List<Character> result_line_1 = new ArrayList<Character>();
		List<Character> result_line_2 = new ArrayList<Character>();

		for (int i=0; i<len_1; i++) {
			if (match_line_1[i]>0) {
				result_line_1.add(sen_1.charAt(i));
			}
		}
		
		for (int i=0; i<len_2; i++) {
			if (match_line_2[i]>0) {
				result_line_2.add(sen_2.charAt(i));
			}
		}
		
		int t = 0;
		int result_len = result_line_1.size();
		for (int i=0; i<result_len; i++) {
			if (!result_line_1.get(i).equals(result_line_2.get(i))) {
				t += 1;
			}
		}

		int l = 0;
		while (l < sen_1.length() && l < sen_2.length() && sen_1.charAt(l) == sen_2.charAt(l)) {
			l += 1;
		}
		
		double dj = m/3.0/len_1 + m/3.0/len_2 + (m-t)/3.0/m;
		double dw = dj + l*0.1*(1-dj);

		return dw;
	}
	
	public static  Map<String,Integer>[] get_onehot_vector(String [] sentence_1, String [] sentence_2) {
		Set<String> words = new HashSet<String>();
		
		for (String w : sentence_1) {
			words.add(w);
		}
		for (String w : sentence_2) {
			words.add(w);
		}

		Map<String,Integer> x = new HashMap<String, Integer>();
		Map<String,Integer> y = new HashMap<String, Integer>();

		for (String w : words) {
			x.put(w,0);
			y.put(w,0);
		}

		for (String w : sentence_1) {
			x.put(w,x.get(w)+1);
		}
		for (String w : sentence_2) {
			y.put(w,y.get(w)+1);
		}

		@SuppressWarnings("unchecked")
		Map<String,Integer>[] m = ((Map<String,Integer>[]) new Object[2]); 
		m[0] = x;
		m[1] = y;
		return m;
	}
	
	public static int get_manhattan_distance(String[] sentence_1, String[] sentence_2) {
		Map<String,Integer>[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String,Integer> x = m[0];
		Map<String,Integer> y = m[1];

		int dis = 0;
		for (String w : x.keySet()){
			dis += Math.max(x.get(w) - y.get(w), y.get(w) - x.get(w));
		}

		return dis;
	}
	
	public static double get_euclidean_distance(String[] sentence_1, String[] sentence_2) {
		Map<String,Integer>[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String,Integer> x = m[0];
		Map<String,Integer> y = m[1];

		int dis = 0;
		for (String w : x.keySet()){
			dis += (x.get(w) - y.get(w)) * (y.get(w) - x.get(w));
		}

		return Math.sqrt(dis);
	}
	
	public static double get_cosine_distance(String[] sentence_1, String[] sentence_2) {
		Map<String,Integer>[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String,Integer> x = m[0];
		Map<String,Integer> y = m[1];
		
		int len_x = 0;
		int len_y = 0;
		int xy = 0;
		
		for (String w : x.keySet()){
			len_x += x.get(w) * x.get(w);
			len_y += y.get(w) * y.get(w);
			xy += x.get(w) * y.get(w);
		}

		if (0==len_x || 0==len_y) {
			return 0.0;
		}
		return xy/Math.sqrt(len_x)/Math.sqrt(len_y);
	}

	public static int get_ngram_distance(String[] sentence_1, String[] sentence_2) {
		int len_1=sentence_1.length;
		int len_2=sentence_2.length;
		String[] mod_sentence_1 = new String[len_1-2];
		String[] mod_sentence_2 = new String[len_2-2];

		for (int i=0; i<len_1-2; i++) {
			mod_sentence_1[i]= String.join(" ", sentence_1[i], sentence_1[i+1], sentence_1[i+2]);
		}
		
		for (int i=0; i<len_2-2; i++) {
			mod_sentence_2[i]= String.join(" ", sentence_2[i], sentence_2[i+1], sentence_2[i+2]);
		}

		return get_manhattan_distance(mod_sentence_1, mod_sentence_2);
	}

	public static int get_matching_coefficient(String[] sentence_1, String[] sentence_2) {
		Map<String,Integer>[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String,Integer> x = m[0];
		Map<String,Integer> y = m[1];

		int cnt_xy = 0;
		for (String w : x.keySet()) {
			if (null != x.get(w) && 0 != x.get(w) && null != y.get(w) && 0 != y.get(w)){
				cnt_xy += 1;
			}
		}
		return cnt_xy;
	}
		
	public static double get_dice_coefficient(String[] sentence_1, String[] sentence_2) {
		Map<String,Integer>[] m = get_onehot_vector(sentence_1, sentence_2);
		Map<String,Integer> x = m[0];
		Map<String,Integer> y = m[1];
		
		int cnt_xy = 0;
		int cnt_x = 0;
		int cnt_y = 0;
		for (String w : x.keySet()) {
			if (null != x.get(w) && 0 != x.get(w)){
				cnt_x += 1;
			}
			if (null != y.get(w) && 0 != y.get(w)){
				cnt_y += 1;
			}
			if (null != x.get(w) && 0 != x.get(w) && null != y.get(w) && 0 != y.get(w)){
				cnt_xy += 1;
			}
		}

		if ( 0 == cnt_x + cnt_y) {
			return 0;
		}

		return 2.0*cnt_xy/(cnt_x + cnt_y);
	}
}
