/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package phrasesentimentextractor;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
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
        InputStream is = new FileInputStream("en-token.bin");
                        
        TokenizerModel model = new TokenizerModel(is);
	Tokenizer tokenizer = new TokenizerME(model);
        
        //POS model from OpenNLP, gives the POS tags
        POSModel posmodel = new POSModelLoader().load(new File("en-pos-maxent.bin"));
        POSTaggerME tagger = new POSTaggerME(posmodel);
        
        DependencyTreeGenerator dr = DependencyTreeGenerator.getInstance();
        TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
        
        //chunker
        is = new FileInputStream("en-chunker.bin");
	ChunkerModel cModel = new ChunkerModel(is);
        ChunkerME chunkerME = new ChunkerME(cModel);
        
        
        //Start processing the review file
        
        //Extract all the features
        ArrayList<String> features = new ArrayList();
        File feat_input = new File(args[0]);
        Scanner scanner = new Scanner(feat_input);
        int feat_counter = 0;
        while(scanner.hasNext()){
            features.add(scanner.nextLine().trim());
            feat_counter++;
        }
        
        String sentence = "";
        String tokens[] = null;
	tokens = tokenizer.tokenize(sentence);
        for(String token : tokens){
            System.out.print(token+" ");
        }
        System.out.println("\nTags\n");
        String tags[] = null;
        
        
        
        File review_text = new File(args[1]);
        FileReader fileReader = new FileReader(review_text);
        
        DocumentPreprocessor dp = new DocumentPreprocessor(fileReader);
        dp.setTokenizerFactory(tokenizerFactory);
        int num_lines = 0;
        for (List line : dp) {
            sentence = Sentence.listToString(line);
            System.out.println(sentence);
            tokens = tokenizer.tokenize(sentence);
                    
          
            
            tags = tagger.tag(tokens);
        
        for(String tag : tags){
            System.out.print(tag+" ");
        }
        
                
        System.out.println("\nBIO Encoding\n");
        //BIO encoding for sentence

	String result[] = chunkerME.chunk(tokens, tags);
        for(String r: result){
            System.out.print(r+" ");
        }
        
        System.out.println("\nPhrases\n");
        //Outputs spans of BIO-NP
        
        HashMap<Integer, Integer> span_map = new HashMap();
        Span[] span = chunkerME.chunkAsSpans(tokens, tags);
        int j = 0;
        
        
        ArrayList<PhraseSet> pSets = new ArrayList();
        
	for (Span s : span){
                
                ArrayList<String> words = new ArrayList();
		System.out.print("\n"+s.toString()+" ");
                int n=0;
                for(int i= s.getStart(); i<s.getEnd();i++){
                    System.out.print(tokens[i]+" ");
                    span_map.put(i, j);
                    words.add(tokens[i]);
                    n++;
                }
                
                PhraseSet pSet = new PhraseSet(j,s.toString(), words);
                pSets.add(pSet);
                
                
                j++;
        }
        
        
        System.out.println("\nTyped Dependencies\n");
        //get dependency tree
        DependencyTree depTree = dr.getTypedDependencyTree(sentence);
        
        //RootWord //Actual root is dummy
        DependencyTreeNode rootNode = depTree.getVertex(0).edges.get(0).target;;
        Queue<DependencyTreeNode> queue = new LinkedList();
        
        queue.add(rootNode);
        
        while(!queue.isEmpty()){
            
            DependencyTreeNode u = queue.remove();
            
            if(span_map.get(u.index-1)!=null){
                u.phrase_index = span_map.get(u.index-1);
            }
            else{
                u.phrase_index = -1;
            }
            System.out.println("\n"+u.word+"-"+u.phrase_index+"-"+tags[u.index-1]);
            for(DependencyTreeEdge e : u.edges){
                queue.add(e.target);
                System.out.print(e.target.word+" ");
            }          
        
        
        }
            
            num_lines++;
        }
        
        System.out.println(num_lines);
        
        /*tags = tagger.tag(tokens);
        
        for(String tag : tags){
            System.out.print(tag+" ");
        }
        
                
        System.out.println("\nBIO Encoding\n");
        //BIO encoding for sentence

	String result[] = chunkerME.chunk(tokens, tags);
        for(String r: result){
            System.out.print(r+" ");
        }
        
        System.out.println("\nPhrases\n");
        //Outputs spans of BIO-NP
        
        HashMap<Integer, Integer> span_map = new HashMap();
        Span[] span = chunkerME.chunkAsSpans(tokens, tags);
        int j = 0;
        
        
        ArrayList<PhraseSet> pSets = new ArrayList();
        
	for (Span s : span){
                
                ArrayList<String> words = new ArrayList();
		System.out.print("\n"+s.toString()+" ");
                int n=0;
                for(int i= s.getStart(); i<s.getEnd();i++){
                    System.out.print(tokens[i]+" ");
                    span_map.put(i, j);
                    words.add(tokens[i]);
                    n++;
                }
                
                PhraseSet pSet = new PhraseSet(j,s.toString(), words);
                pSets.add(pSet);
                
                
                j++;
        }
        
        
        System.out.println("\nTyped Dependencies\n");
        //get dependency tree
        DependencyTree depTree = dr.getTypedDependencyTree(sentence);
        
        //RootWord //Actual root is dummy
        DependencyTreeNode rootNode = depTree.getVertex(0).edges.get(0).target;;
        Queue<DependencyTreeNode> queue = new LinkedList();
        
        queue.add(rootNode);
        
        while(!queue.isEmpty()){
            
            DependencyTreeNode u = queue.remove();
            
            if(span_map.get(u.index-1)!=null){
                u.phrase_index = span_map.get(u.index-1);
            }
            else{
                u.phrase_index = -1;
            }
            System.out.println("\n"+u.word+"-"+u.phrase_index+"-"+tags[u.index-1]);
            for(DependencyTreeEdge e : u.edges){
                queue.add(e.target);
                System.out.print(e.target.word+" ");
            }          
        
        
        }*/
        
        
        System.out.println("Success");
        
        
        
        
    }
    
}
