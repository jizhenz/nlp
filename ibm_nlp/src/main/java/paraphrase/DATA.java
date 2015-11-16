package paraphrase;

public enum DATA {
	 Original("The array of tokens of original string, with original order maintained")
	,Stem("As in Original, but tokens are replaced by their stems")
	,POS("As in Original, but tokens are replaced by their part-of-speech (POS) tags")
	,Soundex("As in Original, but tokens are replaced by their soundex codes")
	,Nouns("The array of tokens of nouns of original string")
	,NounStem("As in Nouns, but tokens are replaced by their stems")
	,NounSoundex("As in Nouns, but tokens are replaced by their soundex codes")
	,Verbs("The array of tokens of verbs of original string")
	,VerbStem("As in Verbs, but tokens are replaced by their stems")
	,VerbSoundex("As in Verbs, but tokens are replaced by their soundex codes");
	
	private final String description;   
	DATA(String description) {
       this.description = description;
   }
   
   public String getDescription() {
   	return this.description;
   }
}
