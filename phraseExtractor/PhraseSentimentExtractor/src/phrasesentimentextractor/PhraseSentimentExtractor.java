/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package phrasesentimentextractor;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.Span;

/**
 *
 * @author Majisha
 */

/*
1. Tokenize each sentence
2. Perform POS
3. Perform chunking
4. get dependency tree
*/
public class PhraseSentimentExtractor {

    /**
     * @param args the command line arguments
     */
    
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        
        //Initialize all the models
        //Tokenizer model for the sentence from OpenNLP , tokenizes the sentence
//        InputStream is = new FileInputStream("en-token.bin");
//                        
//        TokenizerModel model = new TokenizerModel(is);
//	Tokenizer tokenizer = new TokenizerME(model);
//        
//        //POS model from OpenNLP, gives the POS tags
//        POSModel posmodel = new POSModelLoader().load(new File("en-pos-maxent.bin"));
//        POSTaggerME tagger = new POSTaggerME(posmodel);
        
        DependencyTreeGenerator dr = DependencyTreeGenerator.getInstance();
        TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
        
        //chunker
        Path filepath = Paths.get("models/en-chunker.bin");
        InputStream is = new FileInputStream(filepath.toFile());
	ChunkerModel cModel = new ChunkerModel(is);
        ChunkerME chunkerME = new ChunkerME(cModel);
        
        
        //Output file 
        File output_phrases = new File(args[2]);
        FileWriter fout = new FileWriter(output_phrases);
        PrintWriter out = new PrintWriter(fout);
        
        //Start processing the review file
        
        //Extract all the features
        Set<String> features = new HashSet();
        
        File feat_input = new File(args[0]);
        Scanner scanner = new Scanner(feat_input);
        int feat_counter = 0;
        while(scanner.hasNext()){
            features.add(scanner.nextLine().trim());
            feat_counter++;
        }
        String sentence ="";

        File review_text = new File(args[1]);
        FileReader fileReader = new FileReader(review_text);
        
        DocumentPreprocessor dp = new DocumentPreprocessor(fileReader);
        dp.setTokenizerFactory(tokenizerFactory);
        int num_lines = 0;
        
        for (List line : dp) {
            boolean feature_exists = false;
//            
            sentence = Sentence.listToString(line);
                Set<String> check_features = new HashSet();
                for(String feature: features){
                    Pattern pattern = Pattern.compile("\\b"+feature.toLowerCase()+"\\b", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(sentence.toLowerCase());
                    while(matcher.find()){
                        feature_exists = true;
                        check_features.add(feature);
                    }


                }
                if (!feature_exists){
                    //System.out.println("\n"+sentence);
                    //System.out.println("No feature present!\n");
                    continue;
                }

                //Features present
                //System.out.println("\nFeatures present\n");
//                for(String feature : check_features){
//                    //System.out.print(feature+" ");
//                }
            
            
            //get parse tree and construct dependency tree    
        Tree tr = dr.parse(sentence);
        DependencyTree depTree = dr.getTypedDependencyTree(tr);
        
        //get tokenized words
        //System.out.println("\nTokenized Words\n");
        List<Word> word_list = tr.yieldWords();
        List<String> word_tokens = new ArrayList();
        for(Word word : word_list){
            word_tokens.add(word.word());
            //System.out.print(word.word()+" ");
        }
        String[] words = new String[word_tokens.size()];
        words = word_tokens.toArray(words);
        
        
        
        //System.out.println("\nPOS Tags\n");
        List<TaggedWord> postags = tr.taggedYield();
        List<String> tag_tokens = new ArrayList();
        for(TaggedWord postag : postags){
            tag_tokens.add(postag.tag());
            System.out.print(postag.tag()+" ");
        }
        String[] tags = new String[tag_tokens.size()];
        tags = tag_tokens.toArray(tags);
        
                
        //System.out.println("\nBIO Encoding\n");
        //BIO encoding for sentence

	String result[] = chunkerME.chunk(words, tags);
        for(String r: result){
            System.out.print(r+" ");
        }
        
        //System.out.println("\nPhrases\n");
        //Outputs spans of BIO-NP

        HashMap<Integer, Integer> span_map = new HashMap();
        Span[] span = chunkerME.chunkAsSpans(words, tags);
        int j = 0;
        
        
        ArrayList<PhraseSet> pSets = new ArrayList();
        
	for (Span s : span){
                
                ArrayList<String> phrase_words = new ArrayList();
		//System.out.print("\n"+s.toString()+" ");
                int n=0;
                for(int i= s.getStart(); i<s.getEnd();i++){
                    System.out.print(words[i]+" ");
                    span_map.put(i, j);
                    phrase_words.add(words[i]);
                    n++;
                }
                
                PhraseSet pSet = new PhraseSet(j,s.toString(), phrase_words);
                pSets.add(pSet);
                
                
                j++;
        }
        
        
        
        
        //RootWord //Actual root is dummy

        DependencyTreeNode rootNode = depTree.getVertex(0).edges.get(0).target;
        Queue<DependencyTreeNode> queue = new LinkedList();
        rootNode.parent = null;
        queue.add(rootNode);
        
        while(!queue.isEmpty()){
            
            DependencyTreeNode u = queue.remove();
            u.pos = tags[u.index-1];
            if(span_map.get(u.index-1)!=null){
                u.phrase_index = span_map.get(u.index-1);
                
            }
            else{
                u.phrase_index = -1;
            }
            //System.out.println("\n"+u.word+"-"+u.phrase_index+"-"+tags[u.index-1]);
            for(DependencyTreeEdge e : u.edges){
                e.target.parent = u;
                queue.add(e.target);
                //System.out.print(e.target.word+" ");
            }          
        
        
        } 
        
        SentimentExtract.getSentimentPhrases(check_features,pSets,depTree);
        
        
            
            num_lines++;
        }
        
        System.out.println(num_lines);
        
        out.println("Success");
        System.out.println("Success");
        out.close();
        
        
        
        
    }
    
}
