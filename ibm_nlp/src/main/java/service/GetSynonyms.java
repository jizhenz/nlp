package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wordnet.WordnetClient;

public class GetSynonyms {

    private final String word;
    private final WordnetClient wc;

    public GetSynonyms(String word) throws Exception {
        this.word = word;
		this.wc = WordnetClient.getWordnetClient();
    }

    public List<Map<String,String>> getSynonyms() {
    	return this.wc.getSynonyms(this.word);
    }
}
