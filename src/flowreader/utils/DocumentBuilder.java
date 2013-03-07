/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.WordCloud;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jim
 */
abstract class DocumentBuilder implements FileReader {
    protected HashMap<String, Integer> commonWords;
    protected HashMap<String, Integer> documentOccurrences; //dict for whole document
    protected ArrayList<Integer> pageWordCounts; //array storing the word count for each page in the book
    protected int wordCount = 0;
    protected File file;
    
    public DocumentBuilder() {
        this.commonWords = new HashMap<>();
        this.documentOccurrences = new HashMap<>();
        this.pageWordCounts = new ArrayList<>();
       // this.getCommonWords();
    }
    
      //returns a hashmap of strings in the provided text with their frequency count
    public HashMap<String, Integer> getWordsOccurrences(String text){
        String[] words = text.split(" ");
        HashMap<String,Integer> wordsOccurrences = new HashMap();
        for(int i = 0; i < words.length; i++){
            this.wordCount++;
            String word = trimPunctuation(words[i]);
            if (!this.commonWords.containsKey(word)) {
                            if(wordsOccurrences.get(word)!=null){
                                this.addDocumentOccurrence(word);
                                wordsOccurrences.put(word, wordsOccurrences.get(word)+1);
                            }
                            else{
                                
                                this.addDocumentOccurrence(word);
                                wordsOccurrences.put(word, 1);
                                
                            }
                        }
        }
        return wordsOccurrences;
        
    }
      protected ArrayList<ArrayList<WordCloud>> makeCloudLevels(ArrayList<WordCloud> clouds){
        ArrayList<ArrayList<WordCloud>> localLevels = new ArrayList<ArrayList<WordCloud>>();
        ArrayList<ArrayList<WordCloud>> otherLevels = new ArrayList<ArrayList<WordCloud>>();
        ArrayList<WordCloud> currentLevel = new ArrayList<WordCloud>();
        WordCloud cloudB = null;
        boolean haveB = false;
        int listCount = 0;
        int lastIndex = clouds.size() -1;
        if (lastIndex == 0){  // only one cloud in array
           // return clouds;
            localLevels.add(clouds);
            return localLevels;
            
        }
      
        for (WordCloud cloud : clouds){
            
             if (haveB && (cloudB!= null)){      
                 WordCloud newCloud = new WordCloud(cloud, cloudB);
                 if (clouds.indexOf(cloud) + 1 == lastIndex){
                    WordCloud triCloud = new WordCloud(newCloud, clouds.get(lastIndex));
                    currentLevel.add(triCloud);
                 }
                 else{
                currentLevel.add(newCloud);   
                 }
                haveB = false;
            }
             else{
                 cloudB = cloud;
                 haveB = true;
             }
            
        }
        localLevels.add(currentLevel);
        otherLevels = makeCloudLevels(currentLevel);
        for (ArrayList<WordCloud> cloudList : otherLevels){ // add otherLevels to localLevels
            localLevels.add(cloudList);
        }
        return localLevels;
        
    }
      protected void getCommonWords() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(System.getProperty("user.dir")+System.getProperty("file.separator")+"CommonEnglishWords.txt"));
            
            String temp_text;
            while ((temp_text = bufferedReader.readLine()) != null) {
                //System.out.println(temp_text);
                this.commonWords.put(temp_text, 1);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Couldn't find the file!");
        } catch (IOException ex) {
            System.out.println("no idea!.. some IOException");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                System.out.println("couldn't close the file!");
            }
        }
    }
    //adds a word's occurrence to the document Occurrences Map
    protected void addDocumentOccurrence(String word){
        if (this.documentOccurrences.containsKey(word)){
            int count = this.documentOccurrences.get(word);
            count++;
            this.documentOccurrences.put(word, count);
        }
        else{
            this.documentOccurrences.put(word, 1);
        }
        
        //finally, increment the overall word count
        this.wordCount++;
        
    }
    //removes punctuation from any words found
    protected String trimPunctuation(String word) {
        return word.toLowerCase().replaceAll("\\W", "");
    }
    
    
}
