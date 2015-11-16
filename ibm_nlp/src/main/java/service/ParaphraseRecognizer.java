package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paraphrase.WekaRandomForestPredictor;

public class ParaphraseRecognizer {
	private final String phrase1;
	private final String phrase2;
	private boolean isPara;
	private WekaRandomForestPredictor predictor = null;
	
	public ParaphraseRecognizer(String phrase1, String phrase2) throws Exception {
		this.phrase1 = phrase1;
		this.phrase2 = phrase2;
		this.predictor = new WekaRandomForestPredictor();
		this.isPara = this.predictor.predict(this.phrase1, this.phrase2);
	}
	
	public List<Map<String,String>> getPara() {
		List<Map<String,String>> wordList = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		map = new HashMap<String,String>();
		map.put("para",this.isPara ? "Yes" : "No");
		wordList.add(map);
    	return wordList;
	}
}
