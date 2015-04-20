import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureExtractor {
	
	static String taggedReviewFile = "data/ReviewTagged.txt";
	static String extractedTopicFile = "data/Topics.txt";
	static TreeMap<String, Integer> featureSet = new TreeMap();
	static HashSet<String> stopwords;
	
	static {
		try {
			stopwords = new HashSet<String>();
			BufferedReader stopwordsFile = new BufferedReader(new FileReader(new File("data/en.txt")));
			String line = null;
			while((line=stopwordsFile.readLine()) != null) {
				stopwords.add(line);
			}
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws IOException {
		
		int numFeatures = 10, numIterations = 1;
		if(Arrays.asList(args).contains("--mulitple-files")) {
			
		}
		
		if(Arrays.asList(args).contains("--num-iterations")) {
			numIterations = Integer.parseInt(args[Arrays.asList(args).indexOf((String)"--num-iterations") + 1]);
		}
		
		if(Arrays.asList(args).contains("--num-features")) {
			numFeatures = Integer.parseInt(args[Arrays.asList(args).indexOf((String)"--num-features") + 1]);
		}
		
		String inputReviewFile = args[0];
		String outputFeatureFile = args[1];
		
		
		POStagReviews posTagger = new POStagReviews(inputReviewFile, taggedReviewFile);
		try {
			posTagger.tagReviews();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Arrays.asList(args).contains("--filter-nouns")) {
			posTagger.filterNouns(taggedReviewFile, "data/ReviewTaggedFiltered.txt");
			taggedReviewFile = "data/ReviewTaggedFiltered.txt";
		}
		
		TopicModeller topicModel = new TopicModeller(taggedReviewFile, extractedTopicFile);
		topicModel.importDir();
		
		for(int i=0; i<numIterations; i++) {
			topicModel.trainTopics();
			generateFeatures();
			System.out.println(featureSet.size());
		}
		featureSet = sortByRelevance();
		writeFeatures(numFeatures);
		
		
		
	}

	public static void generateFeatures() throws IOException {
		
		BufferedReader topicReader = new BufferedReader(new FileReader(new File(extractedTopicFile)));
		BufferedReader reviewReader = new BufferedReader(new FileReader(new File(taggedReviewFile)));
		
		getWords(topicReader);
		updateCount(reviewReader);
		
	}
	
	public static void getWords(BufferedReader topicReader) throws IOException {
		Pattern p = Pattern.compile("^([a-z]+)([A-Z]+)");
		String line = null;
		while((line=topicReader.readLine()) != null) {
			String[] tokens = line.split(" |\t");
			for(String token : tokens) {
				if(token.endsWith("NN") || token.endsWith("NNP")) {
					Matcher m = p.matcher(token);
					if(m.find()) {
						featureSet.put(m.group(1), 0);
					}
				}
			}
		}
	}
	
	public static void updateCount(BufferedReader reviewReader) throws IOException {
		Pattern p = Pattern.compile("^([a-z]+)([A-Z]+)");
		String line = null;
		while((line=reviewReader.readLine()) != null) {
			String[] tokens = line.split(" ");
			for(String token : tokens) {
				if(token.endsWith("NN") || token.endsWith("NNP")) {
					Matcher m = p.matcher(token);
					if(m.find()) {
						if(featureSet.get(m.group(1)) != null) {
							featureSet.put(m.group(1), featureSet.get(m.group(1))+1);
						}
					}
				}
			}
		}
	}
	
	public static TreeMap sortByRelevance() {
		ValueComparator vc =  new ValueComparator(featureSet);
		TreeMap<String,Integer> sortedMap = new TreeMap<String,Integer>(vc);
		sortedMap.putAll(featureSet);
		return sortedMap;
	}
	
	public static void writeFeatures(int numFeatures) throws IOException {
		BufferedWriter featureWriter = new BufferedWriter(new FileWriter(new File("data/Features.txt")));
		
		for(Map.Entry<String,Integer> entry : featureSet.entrySet()) {
			if(numFeatures == 0)
				break;
			
			String key = entry.getKey();
			if(stopwords.contains(key))
				continue;
			featureWriter.write(key);
			
//			Integer value = entry.getValue();
//			featureWriter.write(" " + value);
			
			featureWriter.newLine();
			numFeatures -= 1;
		}
		featureWriter.close();
	}
	
}

class ValueComparator implements Comparator<String> {
	 
    Map<String, Integer> map;
 
    public ValueComparator(Map<String, Integer> base) {
        this.map = base;
    }
 
    public int compare(String a, String b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
    }
}