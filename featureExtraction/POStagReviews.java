import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class POStagReviews {
	
	File inputFile, outputFile;
	
	public POStagReviews(String inputFile, String outputFile) {
		this.inputFile = new File(inputFile);
		this.outputFile = new File(outputFile);
	}
	
	public void tagReviews() throws IOException, ClassNotFoundException {
		
		BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
		String taggedLine = null;
		MaxentTagger tagger = new MaxentTagger("models/bidirectional-distsim-wsj-0-18.tagger");
		String inputLine = null;
		
		while((inputLine = inputReader.readLine()) != null) {
			taggedLine = tagger.tagString(inputLine);
			String[] taggedWords = taggedLine.split(" ");
			StringBuilder outputLineBuilder = new StringBuilder();
			for(String pair : taggedWords) {
				outputLineBuilder.append(pair.split("/")[0].toLowerCase() + pair.split("/")[1] + " ");
			}
			String outputLine = new String(outputLineBuilder);
			outputWriter.write(outputLine);
			outputWriter.newLine();
		}
		outputWriter.close();
	}
	
	public void filterNouns(String inputFile, String outputFile) throws IOException {
		BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
		String inputLine = null;
		while((inputLine = inputReader.readLine()) != null) {
			String[] taggedWords = inputLine.split(" ");
			for(String tokens : taggedWords) {
				if(tokens.endsWith("NN") || tokens.endsWith("NNP")) {
					outputWriter.write(tokens + " ");
				}
			}
			outputWriter.newLine();
		}
		
		outputWriter.close();
	}
	
}
