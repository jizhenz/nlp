package wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class WordnetClient {

	public String testDictionary() throws IOException {
		
		String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" +currentDir);
        
		// construct the URL to the Wordnet dictionary directory
		String path = "http://localhost:8080/wordnet/dict";
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

}