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
        
        String tokens[] = null;
                
        TokenizerModel model = new TokenizerModel(is);
	Tokenizer tokenizer = new TokenizerME(model);
	tokens = tokenizer.tokenize("The Canon Powershot is the most powerful camera, I have used.");
        
        String tags[] = null;
        
        //POS model from OpenNLP, gives the POS tags
        POSModel posmodel = new POSModelLoader().load(new File("en-pos-maxent.bin"));
        POSTaggerME tagger = new POSTaggerME(posmodel);
        tags = tagger.tag(tokens);
        
        
        //chunker
        is = new FileInputStream("en-chunker.bin");
	ChunkerModel cModel = new ChunkerModel(is);
        
        
        //BIO encoding for sentence
	ChunkerME chunkerME = new ChunkerME(cModel);
	String result[] = chunkerME.chunk(tokens, tags);
        for(String r: result){
            System.out.println(r);
        }
        
        //Outputs spans of BIO-NP
        Span[] span = chunkerME.chunkAsSpans(tokens, tags);
	for (Span s : span)
		System.out.println(s.toString());
        
        System.out.println("Success");
        
        
        
        
    }
    
}
