import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


public class TopicModeller {
	
	String inputFile, outputFile;
	
	public TopicModeller(String inputFile, String outputFile) {
		this.inputFile =inputFile;
		this.outputFile = outputFile;
	}
	
	public void modelTopics() {
		importDir();
		trainTopics();
	}
	
	public void importDir() {
		String command = "../topicmodelling/mallet-2.0.7/bin/mallet "
				+ "import-file --input " + inputFile 
				+ " --output data/topic-pos-input.mallet --keep-sequence --remove-stopwords --preserve-case TRUE";
		String result = executeCommand(command);
		//executeCommand(command);
	}
	
	public void trainTopics() {
		String command = "../topicmodelling/mallet-2.0.7/bin/mallet train-topics "
				+ "--input data/topic-pos-input.mallet --output-topic-keys " + outputFile
				+ " --num-top-words 10 --optimize-interval 20 --num-topics 10";
		String result = executeCommand(command);
	}
	
	private static String executeCommand(String command) {
		 
		StringBuffer output = new StringBuffer();
 		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
 
            String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 		return output.toString();
 	}
	
}
