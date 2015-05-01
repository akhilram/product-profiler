/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package phrasesentimentextractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Majisha
 */
public class SentimentExtract {
    
    static List<PhraseSet> phraseSet;
    static HashMap<String,List<String>> featurePhrases = new HashMap();
    
    
    private static void foundVerb(DependencyTreeNode vertex, String feature, HashMap<Integer,Boolean> phraseSeen){
        List<DependencyTreeEdge> childEdges = vertex.edges;
        DependencyTreeNode parent = vertex.parent;
        
            List<String> f_phrases = featurePhrases.get(feature);
            //check if any of the child is the feature
            for(DependencyTreeEdge e : childEdges){
                if(e.relation_label.contains("subj") || e.relation_label.contains("obj")){
                    DependencyTreeNode child = e.target;
                    if (child.pos.startsWith("NN") || (parent!=null && parent.pos.startsWith("NN"))){
                        if(feature.equals(child.word.toLowerCase()) || (parent!=null && feature.equals(parent.word.toLowerCase()))){

                            for(DependencyTreeEdge other :childEdges){
                     
                                if(!other.equals(e)){
                                    if(other.target.pos.startsWith("JJ") || other.target.pos.startsWith("VB") || other.target.pos.startsWith("RB") || other.target.pos.startsWith("NN")){
                                        if(phraseSeen.get(vertex.phrase_index)!=null){
                                            if(!phraseSeen.get(vertex.phrase_index)){
                                                phraseSeen.put(vertex.phrase_index, Boolean.TRUE);
                                                System.out.println("Verb phrase:");
                                                for(String word : phraseSet.get(vertex.phrase_index).getWords()){
                                                    System.out.print(word+" ");
                                                    f_phrases.add(word);
                                                }
                                            }
                                        }
                                        else{
                                        
                                            System.out.print("Verb phrase:"+vertex.word);
                                            f_phrases.add(vertex.word);
                                        }
                                        if(phraseSeen.get(other.target.phrase_index)!=null){
                                            if(!phraseSeen.get(other.target.phrase_index)){
                                            phraseSeen.put(other.target.phrase_index, Boolean.TRUE);
                                            
                                            System.out.println("\nChildren of verb: "+vertex.word+" Feature: "+child.word+"\nPhrase:");
                                            for(String word : phraseSet.get(other.target.phrase_index).getWords()){
                                                System.out.print(word+" ");
                                                f_phrases.add(word);
                                            }
                                        }}
                                        else{
                                        
                                            System.out.print("Children of verb:"+other.target.word);
                                            f_phrases.add(other.target.word);
                                        }
                                    }
                                }

                            }


                        }
                    }
                    else{
                        if(child.pos.endsWith("DT")){

                                if(vertex.parent!=null && feature.equals(vertex.parent.word.toLowerCase())){

                                    for(DependencyTreeEdge other :childEdges){

                                        if(!other.equals(e)){
                                            if(other.target.pos.startsWith("JJ") || other.target.pos.startsWith("VB") || other.target.pos.startsWith("RB") || other.target.pos.startsWith("NN")){
                                                if(phraseSeen.get(vertex.phrase_index)!=null){
                                                    if(!phraseSeen.get(vertex.phrase_index)){
                                                    phraseSeen.put(vertex.phrase_index, Boolean.TRUE);
                                                    System.out.println("Verb phrase:");
                                                    for(String word : phraseSet.get(vertex.phrase_index).getWords()){
                                                        System.out.print(word+" ");
                                                        f_phrases.add(word);
                                                    }
                                                    
                                                }}
                                                else{
                                                    System.out.println("Verb phrase:"+vertex.word);
                                                
                                                }
                                                if(phraseSeen.get(other.target.phrase_index)!=null){
                                                if(!phraseSeen.get(other.target.phrase_index)){
                                                    phraseSeen.put(other.target.phrase_index, Boolean.TRUE);
                                                    System.out.println("\nChildren of verb: "+vertex.word+" Feature: "+vertex.parent.word+"\nPhrase:");
                                                    for(String word : phraseSet.get(other.target.phrase_index).getWords()){
                                                        System.out.print(word+" ");
                                                        f_phrases.add(word);
                                                    }
                                                
                                                }
                                                }
                                                else{
                                                    System.out.println("Children of verb:"+other.target.word);
                                                    f_phrases.add(other.target.word);
                                                
                                                }
                                            }

                                        }    

                                    }

                                }

                        }
                    }
                    
                }
            }
    }
    
    private static void foundAdjective(DependencyTreeNode vertex, String feature, HashMap<Integer,Boolean> phraseSeen){
        DependencyTreeNode parent = vertex.parent;
            if(parent!=null){

                if(parent.pos.startsWith("VB")){

                    foundVerb(parent, feature, phraseSeen); 
                    

                }
                
                
                if(parent.pos.startsWith("NN")){
                    foundNoun(parent, feature, phraseSeen);
                }

            }
    
    
    }
    
    
    private static void foundAdverb(DependencyTreeNode vertex, String feature, HashMap<Integer,Boolean> phraseSeen){
        DependencyTreeNode parent = vertex.parent;
        
        if(parent!=null){
            

            
            if(parent.pos.startsWith("JJ")){
            
                foundAdjective(parent, feature , phraseSeen);
            }
            
            if(parent.pos.startsWith("VB")){
                foundVerb(parent, feature , phraseSeen);
            }
        
        
        }
         
    
    }
    
    private static void foundNoun(DependencyTreeNode vertex, String feature , HashMap<Integer,Boolean> phraseSeen){
        DependencyTreeNode parent = vertex.parent;
            
            List<String> f_phrases = featurePhrases.get(feature);
            
            if(feature.equals(vertex.word.toLowerCase())){
                
                checkChildrenNoun(vertex,feature,phraseSeen);
                if(parent!=null){
                    if(parent.pos.startsWith("NN")){
                        if(phraseSeen.get(parent.phrase_index)!=null){
                            if(!phraseSeen.get(parent.phrase_index)){
                            phraseSeen.put(parent.phrase_index, Boolean.TRUE);
                            System.out.println("Noun phrases:");
                            for(String word : phraseSet.get(parent.phrase_index).getWords()){
                                System.out.print(word+" ");
                                f_phrases.add(word);
                            }
                    
                        }}
                        else{
                        
                            System.out.print(parent.word);
                            f_phrases.add(parent.word);
                        }
                        checkChildrenNoun(parent, feature, phraseSeen);
                    }
                    
                    if(parent.pos.startsWith("JJ") || parent.pos.startsWith("VB") || parent.pos.startsWith("RB")){
                        if(phraseSeen.get(parent.phrase_index)!=null){
                            if(!phraseSeen.get(parent.phrase_index)){
                            phraseSeen.put(parent.phrase_index, Boolean.TRUE);
                            System.out.println("Noun phrases:");
                            for(String word : phraseSet.get(parent.phrase_index).getWords()){
                                System.out.print(word+" ");
                                f_phrases.add(word);
                            }
                    
                        }}
                        else{
                        
                            System.out.print(parent.word);
                            f_phrases.add(parent.word);
                        }
                        
                        //check their parents as well
                        DependencyTreeNode grandparent = parent.parent;
                        
                        if(grandparent!=null){
                            
                            if(grandparent.pos.startsWith("JJ") || grandparent.pos.startsWith("VB") || grandparent.pos.startsWith("RB")){
                        if(phraseSeen.get(grandparent.phrase_index)!=null){
                            if(!phraseSeen.get(grandparent.phrase_index)){
                            phraseSeen.put(grandparent.phrase_index, Boolean.TRUE);
                            System.out.println("Noun phrases:");
                            for(String word : phraseSet.get(grandparent.phrase_index).getWords()){
                                System.out.print(word+" ");
                                f_phrases.add(word);
                            }
                    
                        }}
                        else{
                        
                            System.out.print(grandparent.word);
                            f_phrases.add(grandparent.word);
                        }
                            }
                        
                        
                        }
                    }
                }
    
            }
            else{
                
                if(parent!=null){
                
                
                if(parent.pos.startsWith("VB")){

                    foundVerb(parent, feature, phraseSeen); 
                    

                }
                
                if(parent.pos.startsWith("RB")){
                
                    foundAdverb(parent,feature,phraseSeen);
                }
                
                if(parent.pos.startsWith("JJ")){
                    foundAdjective(parent, feature, phraseSeen);
                }
                
                
                
                
                
            }
            
            
            }
            
    
    }
    
    
    private static void checkChildrenNoun(DependencyTreeNode vertex, String feature,HashMap<Integer,Boolean> phraseSeen){
        
        List<String> f_phrases = featurePhrases.get(feature);
        
        for(DependencyTreeEdge e: vertex.edges){
            DependencyTreeNode child = e.target;
            if(child.pos.startsWith("JJ") || child.pos.startsWith("VB") || child.pos.startsWith("RB") || child.pos.startsWith("NN")){
                if(phraseSeen.get(child.phrase_index)!=null){
                    
                    if(!phraseSeen.get(child.phrase_index)){
                    phraseSeen.put(child.phrase_index, Boolean.TRUE);
                    System.out.println("Noun phrases:");
                    for(String word : phraseSet.get(child.phrase_index).getWords()){
                        System.out.print(word+" ");
                        f_phrases.add(word);
                    }
                    
                }}
                else{
                    System.out.println("Noun phrases:"+child.word);
                    f_phrases.add(child.word);
                }
                
                
            
            }
        }
        
    }
    
    public static HashMap<String, List<String>> getSentimentPhrases(Set<String> checkFeatures, List<PhraseSet> pSets,DependencyTree depTree){
        
        phraseSet = new ArrayList(pSets);
        featurePhrases = new HashMap();
        
        for(String feature : checkFeatures){
            
            List<String> phraseWords = new ArrayList();
            
            featurePhrases.put(feature,phraseWords);
            
            System.out.println("\nFeature :"+feature+"\nPhrases:");
            
            List<DependencyTreeNode> vertices = depTree.vertices;
            HashMap<Integer,Boolean> phraseSeen = new HashMap();
            
            for(PhraseSet phrase : phraseSet){
                phraseSeen.put(phrase.getPhraseID(),Boolean.FALSE);            
            }

            for(DependencyTreeNode vertex : vertices){
                if (vertex.index !=0){

                    //Rule for nouns
                    if(vertex.pos.startsWith("NN")){
                        foundNoun(vertex, feature, phraseSeen);

                    }

                    //Rule for verbs
                    if(vertex.pos.startsWith("VB")){

                        foundVerb(vertex,feature, phraseSeen);


                    }

                    //Rules for adjective
                    if(vertex.pos.startsWith("JJ")){

                        foundAdjective(vertex, feature , phraseSeen);                          

                    }


                    //Rules for adverb
                    if(vertex.pos.startsWith("RB")){

                        foundAdverb(vertex, feature , phraseSeen);


                    }
                }

            }
        }
        
        return featurePhrases;
    }
}
        
                    
        
        
        


