package paraphrase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

	public Instances load(String relationName, String path, String exportFilePath, int classIndex) 
			throws FileNotFoundException, IOException {
		Instances data = null;
		FeatureGenerator fg = null;
		
		try(BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw = (null != exportFilePath && !exportFilePath.trim().isEmpty()) ? 
					new BufferedWriter(new FileWriter(exportFilePath)) : null) {
		    StringBuilder sb = new StringBuilder();
		    fg = new FeatureGenerator();
		    sb.append(fg.generateArffHeader(relationName));
		    String line = br.readLine();
		    line = br.readLine();
		    while (line != null) {
		        String parts[] = line.split("\t");
		        sb.append(parts[0]);
		        double values[] = fg.generateArffDataRow(parts[3], parts[4]);
		        for (double v : values) {
		        	sb.append(","+v);
		        }
		        sb.append("\n");
		        line = br.readLine();
		    }
		    
		    if (null != bw) {
		    	bw.write(sb.toString());
		    }
		}
		if (null != exportFilePath && !exportFilePath.trim().isEmpty()) {
			data = loadArff(exportFilePath, classIndex);
		}
		return data;
	}
}
