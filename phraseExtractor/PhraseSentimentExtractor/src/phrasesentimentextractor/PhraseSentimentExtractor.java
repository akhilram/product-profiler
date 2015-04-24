/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package phrasesentimentextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
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
        
        //Tokenizer model for the sentence from OpenNLP , tokenizes the sentence
        InputStream is = new FileInputStream("en-token.bin");
        String sentence = "What I found was that the sound quality was not at all excellent and clear and it was certainly comfortable to wear.";
        String tokens[] = null;
                
        TokenizerModel model = new TokenizerModel(is);
	Tokenizer tokenizer = new TokenizerME(model);
        
        DependencyTreeGenerator dr = DependencyTreeGenerator.getInstance();
        
	tokens = tokenizer.tokenize(sentence);
        for(String token : tokens){
            System.out.print(token+" ");
        }
        System.out.println("\nTags\n");
        String tags[] = null;
        
        //POS model from OpenNLP, gives the POS tags
        POSModel posmodel = new POSModelLoader().load(new File("en-pos-maxent.bin"));
        POSTaggerME tagger = new POSTaggerME(posmodel);
        tags = tagger.tag(tokens);
        
        for(String tag : tags){
            System.out.print(tag+" ");
        }
        
        //chunker
        is = new FileInputStream("en-chunker.bin");
	ChunkerModel cModel = new ChunkerModel(is);
        
        
        System.out.println("\nBIO Encoding\n");
        //BIO encoding for sentence
	ChunkerME chunkerME = new ChunkerME(cModel);
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
        
        
        System.out.println("Success");
        
        
        
        
    }
    
}
