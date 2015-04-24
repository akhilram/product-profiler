/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package phrasesentimentextractor;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Majisha
 */
public class DependencyTreeGenerator {
    
    
    private static String PCG_MODEL ;      

    private static TokenizerFactory<CoreLabel> tokenizerFactory ;
    
    private static LexicalizedParser parser ;
    
    private static DependencyTreeGenerator instance = null;
    
    
    //singleton so that model is loaded only once
    private DependencyTreeGenerator(){
        
        PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz"; 
        tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
        parser = LexicalizedParser.loadModel(PCG_MODEL);
    }
    
    public static DependencyTreeGenerator getInstance(){
        
        if(instance == null){
            instance = new DependencyTreeGenerator();
        }
        
        return instance;
        
    }
    
    private static Tree parse(String str) {                
        List<CoreLabel> tokens = tokenize(str);
        Tree tree = parser.apply(tokens);
        return tree;
    }

    private static List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(str));    
        return tokenizer.tokenize();
    }
    
    public DependencyTree getTypedDependencyTree(String sentence){
        Tree tr = parse(sentence);
        TreebankLanguagePack languagePack = new PennTreebankLanguagePack();
        GrammaticalStructure structure = languagePack.grammaticalStructureFactory().newGrammaticalStructure(tr);
        Collection<TypedDependency> typedDependencies = structure.typedDependenciesCollapsed();
          
        
       DependencyTree depTree = new DependencyTree();
       DependencyTreeNode target;
       DependencyTreeNode source;
       
       for(TypedDependency td : typedDependencies) {
            
                System.out.println(td.reln().getLongName() + " " + td.gov().word()+" "+td.gov().index()+" "+td.dep().word()+" "+td.dep().index() );
                
                if(depTree.getVertex(td.gov().index()) == null){
                    source = new DependencyTreeNode(td.gov().index(), td.gov().word());
                }
                else{
                    source = depTree.getVertex(td.gov().index());
                }
                if(depTree.getVertex(td.dep().index()) == null){
                    target = new DependencyTreeNode(td.dep().index(), td.dep().word());
                }
                else{
                    target = depTree.getVertex(td.dep().index());
                }
                
                DependencyTreeEdge edge = new DependencyTreeEdge(td.reln().getShortName(), target);
                source.addEgde(edge);
                depTree.addVertex(source);
                depTree.addVertex(target);
                
                
                
        }
       DependencyTreeNode rootNode = depTree.getVertex(0);
       //System.out.println(rootNode.word);
       DependencyTreeNode rootword = rootNode.edges.get(0).target;
       System.out.println("ROOT :"+rootword.word);
       /*for(DependencyTreeEdge e : rootword.edges){
           System.out.println(e.target.word);
       }*/
       
       return depTree;
    }
}
