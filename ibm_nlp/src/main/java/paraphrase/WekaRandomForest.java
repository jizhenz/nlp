package paraphrase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import weka.classifiers.trees.RandomForest;

public class WekaRandomForest {

	private RandomForest rf;

	public WekaRandomForest() throws IOException, ClassNotFoundException {
		String current = new java.io.File(".").getCanonicalPath();
		String path = current + "/weka/RF.15.model";
		FileInputStream fis = new FileInputStream(path);
		rf = (RandomForest) (new ObjectInputStream(fis)).readObject();

	}

	public String getTagged(String line) {
		String s = null;

		InputStream modelIn = null;
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String path = current + "/opennlp/en-token.bin";
			modelIn = new FileInputStream(path);
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);
			String tokens[] = tokenizer.tokenize(line);
			for (String t : tokens) {
				System.out.println(t);
			}

			POSModel posModel = new POSModelLoader().load(new File(current + "/opennlp/en-pos-maxent.bin"));
			PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
			POSTaggerME tagger = new POSTaggerME(posModel);
			
			String tagged[] = tagger.tag(tokens);

			for (String t : tagged) {
				System.out.println(t);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		return s;
	}

}
