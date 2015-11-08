package service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NlpController {


    @RequestMapping("/getSynonyms")
    public List<Map<String,String>> getSynonyms(@RequestParam(value="word", defaultValue="word") String word) {
    	return new GetSynonyms(word).getSynonyms();
    }
    
    @RequestMapping("/paraphrase")
    public List<Map<String,String>> paraphraseRecognizer(
    		@RequestParam(value="phrase1", defaultValue="") String phrase1
    	   ,@RequestParam(value="phrase2", defaultValue="") String phrase2) {
        return new ParaphraseRecognizer(phrase1, phrase2).getPara();
    }
}
