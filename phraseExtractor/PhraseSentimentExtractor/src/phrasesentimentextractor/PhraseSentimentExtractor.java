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
*/
public class PhraseSentimentExtractor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        
        //Tokenizer model for the sentence from OpenNLP , tokenizes the sentence
        InputStream is = new FileInputStream("en-token.bin");
        String sentence = " It is not the Myota movement anymore, the Myota was 21 jewels and the rotor was plain with just the name etched in it.";
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
        
        
        //BIO encoding for sentence
	ChunkerME chunkerME = new ChunkerME(cModel);
	String result[] = chunkerME.chunk(tokens, tags);
        for(String r: result){
            System.out.println(r);
        }
        
        System.out.println("\nPhrases\n");
        //Outputs spans of BIO-NP
        Span[] span = chunkerME.chunkAsSpans(tokens, tags);
	for (Span s : span){
		System.out.print("\n"+s.toString()+" ");
                for(int i= s.getStart(); i<s.getEnd();i++){
                    System.out.print(tokens[i]+" ");
                }
        }
        
        
        System.out.println("\nTyped Dependencies\n");
        //get dependency tree
        dr.getTypedDependencyTree(sentence);
        
        System.out.println("Success");
        
        
        
        
    }
    
}
