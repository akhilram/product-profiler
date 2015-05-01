/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package phrasesentimentextractor;

/**
 *
 * @author Majisha
 */
public class DependencyTreeEdge {
    
    String relation_label;
    DependencyTreeNode target;
    
    public DependencyTreeEdge(String relation_label, DependencyTreeNode target){
        this.relation_label = relation_label;
        this.target = target;    
    }
    
}
