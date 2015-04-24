/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package phrasesentimentextractor;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author Majisha
 */
public class DependencyTree {
    
    public ArrayList<DependencyTreeNode> vertices;
    
    public DependencyTreeNode getVertex(int index){
       
        ListIterator it = vertices.listIterator();
        while(it.hasNext()){
            DependencyTreeNode v = (DependencyTreeNode) it.next();
            if (v.index == index)
                return v;
        }
        return null;
        
    }
    public DependencyTree(){
        this.vertices = new ArrayList();
    
    }
 
    
    public void addVertex(DependencyTreeNode vertex){
        if(getVertex(vertex.index) == null){
            this.vertices.add(vertex);
                   
        }
    }
}
