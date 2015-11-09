package wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IIndexWordID;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.IndexWordID;
import edu.mit.jwi.item.POS;

public class WordnetClient {

	private IDictionary dict = null;

	private WordnetClient() throws Exception {
		String current = new java.io.File(".").getCanonicalPath();
		String path = current + "/wordnet/dict";
		File wnDir = new File(path);
		IRAMDictionary dict = new RAMDictionary(wnDir, ILoadPolicy.NO_LOAD);
		dict.open();
		// now load into memory
		System.out.print("\nLoading Wordnet into memory ... ");
		long t = System.currentTimeMillis();
		dict.load(true);
		System.out.printf(" done (%1d msec )\n", System.currentTimeMillis() - t);
		this.dict = dict;
	}

	private static WordnetClient singleton = null;

	public static final WordnetClient getWordnetClient() throws Exception {
		if (WordnetClient.singleton == null) {
			singleton = new WordnetClient();
		}
		return singleton;
	}

	public String testDictionary() throws IOException {

		String current = new java.io.File(".").getCanonicalPath();
		System.out.println("Current dir:" + current);
		String currentDir = System.getProperty("user.dir");
		System.out.println("Current dir using System:" + currentDir);

		// construct the URL to the Wordnet dictionary directory
		String path = current + "/wordnet/dict";
		URL url = new URL("file", null, path);

		// construct the dictionary object and open it
		IDictionary dict = new Dictionary(url);
		dict.open();

		// look up first sense of the word "dog "
		IIndexWord idxWord = dict.getIndexWord("dog", POS.NOUN);
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID);
		System.out.println(" Lemma = " + word.getLemma());
		System.out.println(" Gloss = " + word.getSynset().getGloss());
		return "Id = " + wordID + " Lemma = " + word.getLemma() + " Gloss = " + word.getSynset().getGloss();
	}

	public void testRAMDictionary(File wnDir) throws Exception {

		// construct the dictionary object and open it
		IRAMDictionary dict = new RAMDictionary(wnDir, ILoadPolicy.NO_LOAD);
		dict.open();

		// do something slowly
		trek(dict);

		// now load into memory
		System.out.print("\nLoading Wordnet into memory ... ");
		long t = System.currentTimeMillis();
		dict.load(true);
		System.out.printf(" done (%1d msec )\n", System.currentTimeMillis() - t);

		// do the same thing again , only faster
		trek(dict);
	}

	public void trek(IDictionary dict) {
		int tickNext = 0;
		int tickSize = 20000;
		int seen = 0;
		System.out.print(" Treking across Wordnet ");
		long t = System.currentTimeMillis();
		for (POS pos : POS.values()) {
			for (Iterator<IIndexWord> i = dict.getIndexWordIterator(pos); i.hasNext();) {
				for (IWordID wid : i.next().getWordIDs()) {
					seen += dict.getWord(wid).getSynset().getWords().size();
					if (seen > tickNext) {
						System.out.print('.');
						tickNext = seen + tickSize;
					}
				}
				// System.out.print(" IndexWordIterator ");
			}
			System.out.println(" POS ");
		}
		System.out.println("END POS ");
		System.out.printf(" done (%1d msec )\n", System.currentTimeMillis() - t);
		System.out.println("In my trek I saw " + seen + " words ");
	}

	public List<Map<String, String>> getSynonyms(String word) {
		List<Map<String, String>> wordList = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		boolean first = true;
		int cnt = 1;
		for (POS pos : POS.values()) {
			IIndexWord idxWord = dict.getIndexWord(word, pos);
			if (idxWord == null) {
				continue;
			}
			first = true;
			for (IWordID wordID : idxWord.getWordIDs()) { // 1st meaning
				if (first) {
					map = new HashMap<String, String>();
					map.put("id", "" + cnt);
					map.put("synonym", pos.name() + ": ----------");
					wordList.add(map);
					cnt++;
					first = false;
					System.out.println(pos.name());
				}
				IWord iW = dict.getWord(wordID);
				ISynset synset = iW.getSynset();
				for (IWord w : synset.getWords()) {
					map = new HashMap<String, String>();
					map.put("id", "" + cnt);
					map.put("synonym", w.getLemma());
					wordList.add(map);
					cnt++;
					System.out.println(w.getLemma());
				}
			}
		}
		return wordList;
	}
}