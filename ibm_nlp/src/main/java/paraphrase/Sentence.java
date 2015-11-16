package paraphrase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.Tokenizer;
import util.Util;

public class Sentence {
	private final String sentence;
	private final Tokenizer tokenizer;
	private final POSTaggerME tagger;
	private final Stemmer stemmer;

	private String origTokens[]; // 1
	private String tags[]; // 2
	private String stems[]; // 3
	private String nouns[]; // 4
	private String verbs[]; // 5
	private String noun_stems[]; // 6
	private String verb_stems[]; // 7
	private String soundex[]; // 8
	private String noun_soundex[]; // 9
	private String verb_soundex[]; // 10

	private int num_tokens = 0;
	private int num_nouns = 0;
	private int num_verbs = 0;



	public Sentence(String sentence) throws IOException {
		this.sentence = sentence;
		this.tokenizer = Util.getEnTokenizer();
		this.tagger = Util.getEnPOSTaggerME();
		this.stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		this.init();
	}

	public void init() {
		this.origTokens = this.tokenizer.tokenize(this.sentence);
		this.num_tokens = this.origTokens.length;
		this.tags = this.tagger.tag(this.origTokens);
		this.stems = new String[this.origTokens.length];
		for (int i = 0; i < this.origTokens.length; i++) {
			this.stems[i] = (String) this.stemmer.stem((CharSequence) this.origTokens[i]);
		}
		this.num_nouns = 0;
		this.num_verbs = 0;
		for (String t : this.tags) {
			if ('N' == t.charAt(0)) {
				this.num_nouns++;
			}
			if ('V' == t.charAt(0)) {
				this.num_verbs++;
			}
		}
		this.nouns = new String[this.num_nouns];
		this.verbs = new String[this.num_verbs];
		int noun_counter = 0, verb_counter = 0;
		for (int i = 0; i < this.origTokens.length; i++) {
			if ('N' == this.tags[i].charAt(0)) {
				this.nouns[noun_counter++] = this.origTokens[i];
			}
			if ('V' == this.tags[i].charAt(0)) {
				this.verbs[verb_counter++] = this.origTokens[i];
			}
		}
		this.noun_stems = new String[this.num_nouns];
		for (int i = 0; i < this.num_nouns; i++) {
			this.noun_stems[i] = (String) this.stemmer.stem((CharSequence) this.nouns[i]);
		}
		this.verb_stems = new String[this.num_verbs];
		for (int i = 0; i < this.num_verbs; i++) {
			this.verb_stems[i] = (String) this.stemmer.stem((CharSequence) this.verbs[i]);
		}
		this.soundex = new String[this.num_tokens];
		for (int i = 0; i < this.num_tokens; i++) {
			this.soundex[i] = this.soundex(this.origTokens[i]);
		}
		this.noun_soundex = new String[this.num_nouns];
		for (int i = 0; i < this.num_nouns; i++) {
			this.noun_soundex[i] = this.soundex(this.nouns[i]);
		}
		this.verb_soundex = new String[this.num_verbs];
		for (int i = 0; i < this.num_verbs; i++) {
			this.verb_soundex[i] = this.soundex(this.verbs[i]);
		}
	}

	public void print() {
		System.out.println("\nOriginal Tokens:");
		for (String t : this.origTokens) {
			System.out.print(t + "\t");
		}
		System.out.println("\nPos Tags:");
		for (String t : this.tags) {
			System.out.print(t + "\t");
		}
		System.out.println("\nStems:");
		for (String t : this.stems) {
			System.out.print(t + "\t");
		}
		System.out.println("\nNouns:");
		for (String t : this.nouns) {
			System.out.print(t + "\t");
		}
		System.out.println("\nVerbs:");
		for (String t : this.verbs) {
			System.out.print(t + "\t");
		}
		System.out.println("\nNoun Stems:");
		for (String t : this.noun_stems) {
			System.out.print(t + "\t");
		}
		System.out.println("\nVerb Stems:");
		for (String t : this.verb_stems) {
			System.out.print(t + "\t");
		}
		System.out.println("\nSoundex:");
		for (String t : this.soundex) {
			System.out.print(t + "\t");
		}
		System.out.println("\nNoun Soundex:");
		for (String t : this.noun_soundex) {
			System.out.print(t + "\t");
		}
		System.out.println("\nVerb Soundex:");
		for (String t : this.verb_soundex) {
			System.out.print(t + "\t");
		}
	}

	public String[] getOrigTokens() {
		return origTokens;
	}
	
	public String[] getTags() {
		return tags;
	}

	public String[] getStems() {
		return stems;
	}

	public String[] getNouns() {
		return nouns;
	}

	public String[] getVerbs() {
		return verbs;
	}

	public String[] getNoun_stems() {
		return noun_stems;
	}

	public String[] getVerb_stems() {
		return verb_stems;
	}

	public String[] getSoundex() {
		return soundex;
	}

	public String[] getNoun_soundex() {
		return noun_soundex;
	}

	public String[] getVerb_soundex() {
		return verb_soundex;
	}

	/**
	 * 
	 */

	private boolean is_alphabet(char a) {
		if (('a' <= a && a <= 'z') || ('A' <= a && a <= 'Z')) {
			return true;
		}
		return false;
	}

	/**
	 * Retain the first letter of the name && drop all other occurrences of a,
	 * e, i, o, u, y, h, w. Replace consonants with digits as follows (after the
	 * first letter): b, f, p, v = 1 c, g, j, k, q, s, x, z = 2 d, t = 3 l = 4
	 * m, n = 5 r = 6 If two or more letters with the same number are adjacent
	 * in the original name (before step 1), only retain the first letter; also
	 * two letters with the same number separated by 'h' or 'w' are coded as a
	 * single number, whereas such letters separated by a vowel are coded twice.
	 * This rule also applies to the first letter.
	 * 
	 * Iterate the previous step until you have one letter && three numbers. If
	 * you have too few letters in your word that you can't assign three
	 * numbers, append with zeros until there are three numbers. If you have
	 * more than 3 letters, just retain the first 3 numbers.
	 *
	 * @return
	 */
	public String soundex(String word) {
		if (null == word || "" == word.trim()) {
			return null;
		}

		word = word.toLowerCase();
		char nocode[] = { 'a', 'e', 'i', 'o', 'u', 'y', 'h', 'w' };
		char one[] = { 'b', 'f', 'p', 'v' };
		char two[] = { 'c', 'g', 'j', 'k', 'q', 's', 'x', 'z' };
		char three[] = { 'd', 't' };
		char four[] = { 'l' };
		char five[] = { 'm', 'n' };
		char six[] = { 'r' };

		Map<Character, String> sound_map = new HashMap<Character, String>();
		for (char w : nocode) {
			sound_map.put(w, "");
		}
		for (char w : one) {
			sound_map.put(w, "1");
		}
		for (char w : two) {
			sound_map.put(w, "2");
		}
		for (char w : three) {
			sound_map.put(w, "3");
		}
		for (char w : four) {
			sound_map.put(w, "4");
		}
		for (char w : five) {
			sound_map.put(w, "5");
		}
		for (char w : six) {
			sound_map.put(w, "6");
		}

		String temp = "";
		for (int i = 0; i < word.length(); i++) {
			char w = word.charAt(i);
			if (is_alphabet(w)) {
				temp += w;
			}
		}
		word = temp;

		if ("".equals(word)) {
			return "0000";
		}

		while (true) {
			boolean hasAdj = false;
			String word_tmp = "";
			int idx = 0;
			while (idx < word.length()) {
				word_tmp += word.charAt(idx);
				while (true) {
					if ("".equals(sound_map.get(word.charAt(idx)))) {
						break;
					}
					if (idx + 1 < word.length()
							&& sound_map.get(word.charAt(idx)) == sound_map.get(word.charAt(idx + 1))) {
						idx += 1;
						hasAdj = true;
					} else if (idx + 2 < word.length() && (word.charAt(idx + 1) == 'h' || word.charAt(idx + 1) == 'w')
							&& sound_map.get(word.charAt(idx)) == sound_map.get(word.charAt(idx + 2))) {
						idx += 2;
						hasAdj = true;
					} else {
						break;
					}
				}
				idx += 1;
			}

			word = word_tmp;
			if (!hasAdj) {
				break;
			}
		}
		String soundex = ("" + word.charAt(0)).toUpperCase();
		for (int i = 1; i < word.length(); i++) {
			char w = word.charAt(i);
			soundex = soundex + sound_map.get(w);
		}

		if (soundex.length() > 4) {
			return soundex.substring(0, 4);
		} else {
			for (int i = soundex.length(); i < 4; i++) {
				soundex += "0";
			}
			return soundex;
		}
	}

}
