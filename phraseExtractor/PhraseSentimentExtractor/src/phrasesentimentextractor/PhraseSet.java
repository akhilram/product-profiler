/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package phrasesentimentextractor;

import java.util.ArrayList;

/**
 *
 * @author Majisha
 */
public class PhraseSet {
    
        private int id;
        private String phrase_tag;
        private ArrayList<String> words;

        public PhraseSet(int id, String phrase_tag, ArrayList<String> words) {
            this.id = id;
            this.phrase_tag = phrase_tag;
            this.words = new ArrayList(words);
        }   
        
        public int getPhraseID(){
            return this.id;
        }
        
        public String getPhraseTag(){
            return this.phrase_tag;
        }
        
        public ArrayList<String> getWords(){
            return this.words;
        }
    
}
