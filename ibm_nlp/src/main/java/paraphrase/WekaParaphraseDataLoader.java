package paraphrase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaParaphraseDataLoader {

	public WekaParaphraseDataLoader() {
	}

	public Instances loadArff(String path, int classIndex) throws IOException {
		ArffLoader loader = null;
		File f = null;
		Instances data = null;
		try {
			f = new File(path);
			loader = new ArffLoader();
			loader.setFile(f);			
			data = loader.getDataSet();
			if (classIndex >= 0) {
				data.setClassIndex(classIndex);
			}
		} catch (IOException e) {
			try {
				if (null != loader) {
					loader.reset();
					loader = null;
				}
			} catch (Exception s) {
				s.printStackTrace();
			}
			throw e;
		}
		return data;
	}

	public Instances loadArffIncrementally(String path, int classIndex) throws IOException {
		ArffLoader loader = null;
		File f = null;
		Instances structure = null;
		try {
			f = new File(path);
			loader = new ArffLoader();
			loader.setFile(f);			
			structure = loader.getStructure();
			if (classIndex >= 0) {
				structure.setClassIndex(classIndex);
			}
		} catch (IOException e) {
			try {
				if (null != loader) {
					loader.reset();
					loader = null;
				}
			} catch (Exception s) {
				s.printStackTrace();
			}
			throw e;
		}
		return structure;
	}

	public Instances load(String path, String exportFilePath) throws FileNotFoundException, IOException {
		Instances data = null;
		
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    
		    String everything = sb.toString();
		}
		
		return data;
	}
}
