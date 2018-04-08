//Zechariah Speer
//class contains the needed class functions for Dictionary built by Trie

import java.util.*;

public class DictionaryTrie{
   
   private TrieNode mRoot;
   
   private class TrieNode{
      Map<Character, TrieNode> children = new TreeMap<>();
      boolean isAWord = false;
   
   }//end TrieNode class

   public DictionaryTrie(){
      mRoot = new TrieNode();
   }//end prefix tree ctor

   public void insertWord(String s) { 
      insertWord(mRoot, s); 
   }
   
   private void insertWord(TrieNode root, String s) {
   //O(n)
      TrieNode cur = root; 
      for (char ch : s.toCharArray()) {
         TrieNode next = cur.children.get(ch); 
         if (next == null)
            cur.children.put(ch, next = new TrieNode());
         cur = next;
      } 
      cur.isAWord = true;
   } 

   public boolean isWord(String input){
      if(input == null || input.length() == 0)
         System.out.println("There is no input");
       
      TrieNode cur = mRoot;
      for (char ch : input.toCharArray()) { //n
         cur = cur.children.get(ch);
         if (cur == null)
            return false;
      } 
      return cur.isAWord;
   }//end isInputWord
}//end class