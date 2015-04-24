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
public class DependencyTreeNode {
    String word;
    int index;
    int phrase_index;
    ArrayList<DependencyTreeEdge> edges;
    
    public DependencyTreeNode(int index, String word){
        this.word = word;
        this.index = index;
        this.edges = new ArrayList();
    }
    
    public void addEgde(DependencyTreeEdge e){
        this.edges.add(e);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof DependencyTreeNode){
                 DependencyTreeNode c = (DependencyTreeNode)o;
                 
                 return this.index == c.index;
            }
            return false;
        }
    
}
