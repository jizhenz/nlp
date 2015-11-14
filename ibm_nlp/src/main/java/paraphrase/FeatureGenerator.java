package paraphrase;

import java.util.ArrayList;
import java.util.List;

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
}
