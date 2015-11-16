package paraphrase;

public enum FEATURE {
	 Edit("Edit Distance")
	,JW("Jaro Winkler Distance")
	,Soundex("Soundex")
	,Manhattan("Manhattan Distance")
	,Euclidean("Euclidean Distance")
	,Cosine("Cosine Similarity")
	,NGram("NGram Distance")
	,Matching("Matching Coefficient")
	,Dice("Dice Coefficient")
	,Jaccard("Jaccard Coefficient");
	
	private final String description;   
    FEATURE(String description) {
        this.description = description;
    }
    
    public String getDescription() {
    	return this.description;
    }

}
