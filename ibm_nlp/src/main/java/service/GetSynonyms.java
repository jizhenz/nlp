package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSynonyms {

    private final String word;

    public GetSynonyms(String word) {
        this.word = word;
    }

    public List<Map<String,String>> getSynonyms() {
    	List<Map<String,String>> wordList = new ArrayList<Map<String,String>>();
    	Map<String,String> map = null;
    	for (int i=1; i<10; i++) {
    		map = new HashMap<String,String>();
    		map.put("id",""+i);
    		map.put("synonym","word_"+i);
    		wordList.add(map);
    	}
    	return wordList;
    }
}
