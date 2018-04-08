//Zechariah Speer
//class contains the needed class functions for Dictionary built by HashTable

import java.util.*;

public class DictionaryHashTable{
   Hashtable<String,Boolean> table;
   
   public DictionaryHashTable(){
      //O(1) time complexity
      table = new Hashtable();
   }//end ctor
   
   public void insert(String item){
      //time complexity O(1)
      table.put(item, true);
   }//end insert
   
   public boolean isWord(String input){
   // time complexity O(1)
      return table.containsKey(input);
   }//end input
}//end class