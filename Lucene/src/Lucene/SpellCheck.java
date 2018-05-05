package Lucene;

import java.io.File;
import java.io.FileReader;
//import java.io.InputStream;
//import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.search.spell.DirectSpellChecker;

public class SpellCheck {

public static ArrayList<String> spell(String text) throws Exception {
    String dic_path = "C:\\Study_Mac\\Information_Retrieval_Systems\\Project\\engmix_index";
    Directory dir1 = FSDirectory.open(Paths.get(dic_path));
    @SuppressWarnings("resource")
	SpellChecker spellChecker = new SpellChecker(dir1);
    Analyzer analyzer = new StandardAnalyzer();
	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	File x = new File("C:\\Study_Mac\\Information_Retrieval_Systems\\Project\\engmix\\engmix.txt");	
	boolean y = true;
	spellChecker.indexDictionary(new PlainTextDictionary(new FileReader(x)),iwc,y);
    String wordForSuggestions = text;
    int suggestionsNumber = 5;
    
    ArrayList<String> array = new ArrayList<String>();
    
    String[] suggestions = spellChecker.suggestSimilar(wordForSuggestions, suggestionsNumber);
    if (suggestions!=null && suggestions.length>0) {
    	System.out.print("Did you mean: ");
        for (String word : suggestions) {
        	array.add(word);
            System.out.print(word + " ");
        }
      }

	return array;
  }

@SuppressWarnings("static-access")
public static void main(String args[]) throws Exception {
	SpellCheck sp = new SpellCheck();
	sp.spell("confrn");
} 
}
