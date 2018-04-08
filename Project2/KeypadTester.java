import java.util.*;
import java.io.*;

//Zechariah Speer
//this class contains all other methods for the assingments plus the main for testing


public class KeypadTester{

   public static void main(String[] args) throws Exception{
      boolean again = true;
      Scanner kb = new Scanner(System.in);
      
      ArrayList<String> dictionWords = DictionaryReader(openFile("dictionary.txt"));
      DictionaryTrie dictTrie = new DictionaryTrie();
      DictionaryHashTable dictTable = new DictionaryHashTable();
      for(String item : dictionWords){
         dictTrie.insertWord(item);
         dictTable.insert(item);
      }//end for
      
      do{
         ArrayList<Integer> inputArray = getInput(kb);
         InputTree solverTree = makeInputTree(inputArray); //4^n
      
         ArrayList<String> list= solverTree.traversePaths();//4^n
         System.out.println("\nPrinting input list that are words Prefix Tree Dictionary");
         for(int x = 0; x < list.size(); x++){
            if(dictTrie.isWord(list.get(x))){//O(n)
               System.out.println(list.get(x));
            }//end if
         }//end for
         
         System.out.println("\nPrinting input list that are words from Table Dictionary");
         for(String word : list){
            if(dictTable.isWord(word))//O(1)
               System.out.println(word);
         }//end for
         again = inputAnother(kb);
      }while(again);
   }//end main
   
   
   //ask user if they want to enter more numbers
   public static boolean inputAnother(Scanner kb){
      boolean again = true;
      while(again){
         System.out.println("\nWould you like to input another set of Numbers? (y/n) ");
         String str = kb.nextLine();
         if(str.equals("y")){
            return true;
         }//end if
         else if(str.equals("n")){
            return false;
         }//end else if
         else
            System.out.println("Not valid input");
      }//end while
      
      return again; 
   }//end inputAnother

   //gets the input the user puts in and make sure its valid
   public static ArrayList getInput(Scanner kb){
      if(kb == null)
         throw new NullPointerException("The Scanner can't be null.");
     
      ArrayList<Integer> inArray = new ArrayList();
      int num = -1;
      do{ 
         System.out.print("Enter Input: ");
         try{
            num = kb.nextInt();
            kb.nextLine();    
            while(num > 0){
               if(num % 10 != 1 && num %10 != 0){
                  inArray.add(num % 10);
                  num = num /10;
               }//end if
               else{
                  throw new Exception();
               }//end else
            }//end while
         }
         catch(InputMismatchException e){
            System.out.println("Invalid input try again");
            num = -1;
            kb.nextLine();
         }//end catch
         catch(Exception e){
            System.out.println("Invalid input 1s do not have letters to them");
            num = -1;
         }//end catch    
      }while(num == -1);
      
      int end = inArray.size() - 1;
      for(int i = 0; i < end; i++){
         int temp = inArray.get(end);
         inArray.set(end,inArray.get(i));
         inArray.set(i, temp);
         end--;
      }//end for
      
      return inArray;
   }//end method
   
   //gets the letter
   public static ArrayList getLetters(int num){
      ArrayList<String> letters = new ArrayList<>();
      if(num > 1 && num < 10){
         switch(num){
            case 2:
               letters.add("a");
               letters.add("b");
               letters.add("c");
               break;
            
            case 3:
               letters.add("d");
               letters.add("e");
               letters.add("f");
               break;
            
            case 4:
               letters.add("g");
               letters.add("h");
               letters.add("i");
               break;
               
            case 5:   
               letters.add("j");
               letters.add("k");
               letters.add("l");
               break;
         
            case 6:   
               letters.add("m");
               letters.add("n");
               letters.add("o");
               break;
            
            case 7:   
               letters.add("p");
               letters.add("q");
               letters.add("r");
               letters.add("s");
               break;
            
            case 8:   
               letters.add("t");
               letters.add("u");
               letters.add("v");
               break;
            
            case 9:   
               letters.add("w");
               letters.add("x");
               letters.add("y");
               letters.add("z");
               break;
         }//end switch
      }//end if
      return letters;
   }//end method
   
   //opens the file
   public static File openFile(String name){
      File fin = null;
      while(fin == null){ 
         fin = new File(name);
         if(!(fin.exists()))
            fin = null;
      }//end of while
      return fin;
   }//end of openInputFile
   
   //reads the dictionary file
   public static ArrayList<String> DictionaryReader(File fin)throws FileNotFoundException{
      ArrayList<String> diction = new ArrayList();
      if(fin != null){
         try{
            Scanner fileScan = new Scanner(fin);
            while(fileScan.hasNext()){
               String str = fileScan.next();
               diction.add(str.substring(0,str.indexOf(",")));
            }//end while
         
         }//end try
         catch(FileNotFoundException e){
            throw new FileNotFoundException(e.getMessage());
         }//end catch
      }//end if
      return diction;
   }//end reader
   
   //makes the users input tree
   public static InputTree makeInputTree(ArrayList<Integer> inputArray){
      InputTree inputTree = new InputTree();
      for(int i = 0; i < inputArray.size();i++){
         ArrayList<String> letters = getLetters(inputArray.get(i)); 
         if(letters != null){
            if(letters.size() == 4)
               inputTree.insert(letters.get(0),letters.get(1), letters.get(2),letters.get(3));
            else
               inputTree.insert(letters.get(0),letters.get(1), letters.get(2));
         }//end if
      }//end for
      return inputTree;
   }//end method
}//end class