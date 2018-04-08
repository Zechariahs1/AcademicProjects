//Zechariah Speer
//class contains the needed class functions for building the input tree that must be solved

import java.util.*;

public class InputTree{
   
   private Node mRoot;
   
   private class Node{
      Comparable mData;
      Node leftMostChild;
      Node rightSibling;
      Node parent;
   
      Node(Comparable data){
         this();
         mData = data;
      }//end Node
      
      Node(){
         mData = null;
         leftMostChild = null;
         rightSibling = null;
         parent = null;
      }//end Node
      
      
      public void insertFirst(Node cur){
         this.leftMostChild = cur;
         cur.parent = this;
      }//end insertFirst
      
      public void insertNext(Node cur){
         this.rightSibling = cur;
         cur.parent = this.parent;
      }//end insertNext
   }//end Node
   
   public InputTree(){
      mRoot = new Node(""); //1
   }//end ctor
   
   public void insert(Comparable item1, Comparable item2, Comparable item3){
      insert(item1, item2, item3, null);
   }//insert
   
   public void insert(Comparable item1, Comparable item2, Comparable item3, Comparable item4){
      mRoot = insert(item1, item2, item3, item4, mRoot);
   }//end insert
   
   public Node insert(Comparable item1, Comparable item2, Comparable item3, Comparable item4, Node root){
      if(root.leftMostChild == null && root != null){ 
         root.insertFirst(new Node(item1));
         Node cur = root.leftMostChild;
         cur.insertNext(new Node(item2));
         cur = cur.rightSibling;
         cur.insertNext(new Node(item3));
         if(item4 != null){
            cur = cur.rightSibling;
            cur.insertNext(new Node(item4));
         }//end if
         return root;
      }//end if
         
      else{
         int count = 2;
         Node cur = root.leftMostChild; 
         cur = insert(item1, item2, item3, item4, cur);
         while(count > 0){
            cur = cur.rightSibling;
            cur = insert(item1, item2, item3, item4, cur);            
            count--;
         }//end while
         if(cur.rightSibling != null)
            cur.rightSibling = insert(item1, item2, item3, item4, cur.rightSibling);
      }//end else
      return root;
   }//end insert
   
   
   public ArrayList traversePaths(){
      ArrayList<String> thePaths = new ArrayList<>();
      String path = "";
      return traversePaths(mRoot, path, thePaths);
   }//end class 
   
   public ArrayList traversePaths(Node root, String path, ArrayList thePaths){
      if(root.leftMostChild == null && root.mData != null){
         thePaths.add(path + root.mData);
         return thePaths;
      }//end if
      else{
         path += (String)root.mData;
         Node temp = root.leftMostChild;
         while(temp != null){
            traversePaths(temp, new String(path), thePaths);
            temp = temp.rightSibling;
         }//end while
         return thePaths;
      }//end if
   }//end traversePaths
}//end SolverTree