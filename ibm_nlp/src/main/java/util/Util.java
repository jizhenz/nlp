package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Util {

	private static final String enTokenRelPath = "/opennlp/en-token.bin";
	private static final String enPosRelPath = "/opennlp/en-pos-maxent.bin";

	private static java.io.File f = null;
	private static String enTokenPath = null;
	private static InputStream modelIn = null;
	private static TokenizerModel tokenizerModel = null;
	private static Tokenizer tokenizer = null;
	private static POSModel posModel = null;
	private static POSTaggerME tagger = null;

	public static void init() throws IOException {
		if (null == f) {
			f = new java.io.File(".");
		}
		if (null == enTokenPath) {
			enTokenPath = getCurrentPath() + enTokenRelPath;
		}
		if (null == modelIn) {
			modelIn = new FileInputStream(enTokenPath);
		}
		if (null == tokenizerModel) {
			tokenizerModel = new TokenizerModel(modelIn);
		}
		if (null == tokenizer) {
			tokenizer = new TokenizerME(getEnTokenizerModel());
		}
		if (null == posModel) {
			posModel = new POSModelLoader().load(new File(getCurrentPath() + enPosRelPath));
		}
		if (null == tagger) {
			tagger = new POSTaggerME(posModel);
		}
	}

	public static String getCurrentPath() throws IOException {
		return f.getCanonicalPath();
	}

	public static TokenizerModel getEnTokenizerModel() {

		return tokenizerModel;
	}

	public static Tokenizer getEnTokenizer() {
		return tokenizer;
	}

	public static POSTaggerME getEnPOSTaggerME() {
		return tagger;
	}
}
