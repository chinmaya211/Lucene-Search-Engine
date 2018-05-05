package Lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
//import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
//import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
//import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.spell.Dictionary;
/**
 * Servlet implementation class Highlighter_Demo
 */
@SuppressWarnings("unused")
public class Highlighter_Demo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String name = request.getParameter("search");
		System.out.println(name);
		try {
			search(name,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	
	
	@SuppressWarnings({ "deprecation", "unused", "static-access" })
	public void search(String text, HttpServletResponse response) throws Exception {
		//String index = "C:\\\\Users\\\\gochh\\\\data\\\\citeseer2_index";
		String index = "C:\\\\Study_Mac\\\\Information_Retrieval_Systems\\\\citeseer2All\\\\citeseer2_index";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
	  	Directory dir = FSDirectory.open(Paths.get(index));
	  	IndexReader reader = DirectoryReader.open(dir);
	  	IndexSearcher searcher = new IndexSearcher(reader);
	      Analyzer analyzer = new StandardAnalyzer();
	      QueryParser parser = new QueryParser("contents", analyzer);

	      // Wildcard ?
		    if(text.contains ("?"))
		    {
		    	Query query1 = new WildcardQuery(new Term("contents", text));
		    	TopDocs hits = searcher.search(query1, 5, Sort.INDEXORDER);
		    	out.println("<br>" + hits.totalHits + "total matching documents");
		    	out.println("</p>");
		    	 QueryScorer scorer = new QueryScorer(query1);
			      Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:blue;\"><b>","</b></span>");
			      Highlighter highlighter = new Highlighter(formatter, scorer);
			      Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 100);
			      highlighter.setTextFragmenter(fragmenter);
			      		         for (int i = 0; i < hits.scoreDocs.length; i++) {
			          int id = hits.scoreDocs[i].doc;
			          Document doc = searcher.doc(id);
			          String title = doc.get("path");
			          out.println(title);
			          out.println("</p>");
			          String text_word = doc.get("contents");
			          TokenStream stream = TokenSources.getAnyTokenStream(reader, id, "contents", analyzer);
			          String[] frags = highlighter.getBestFragments(stream, text_word, 5);
			          for (int k=0;k<frags.length;k++)
			          {
			              //System.out.println(path);
			              //System.out.println(frag);
			              //out.println("Path: " + doc.get("path"));
			             // out.println("</p>");
			        	  out.print("Abstract: ");
			              out.print(frags[k]+"...");
			              //out.println("contents: " + doc.get("contents"));
			              //out.println("</p>");
			          } 
			          out.println("</p>");
			      }
			      reader.close();
		    }
		 // Wildcard *
		    if(text.contains ("*")) {
		    	Query query1 = new WildcardQuery(new Term("contents", text));
		    	TopDocs hits = searcher.search(query1, 5, Sort.INDEXORDER);
		    	out.println("<br>" + hits.totalHits + "total matching documents");
		    	out.println("</p>");
		    	 QueryScorer scorer = new QueryScorer(query1);
			      Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:blue;\"><b>","</b></span>");
			      Highlighter highlighter = new Highlighter(formatter, scorer);
			      Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 100);
			      highlighter.setTextFragmenter(fragmenter);
			      		         for (int i = 0; i < hits.scoreDocs.length; i++) {
			          int id = hits.scoreDocs[i].doc;
			          Document doc = searcher.doc(id);
			          String title = doc.get("path");
			          out.println(title);
			          out.println("</p>");
			          String text_word = doc.get("contents");
			          TokenStream stream = TokenSources.getAnyTokenStream(reader, id, "contents", analyzer);
			          String[] frags = highlighter.getBestFragments(stream, text_word, 5);
			          for (int k=0;k<frags.length;k++)
			          {
			              //System.out.println(path);
			              //System.out.println(frag);
			              //out.println("Path: " + doc.get("path"));
			             // out.println("</p>");
			        	  out.print("Abstract: ");
			              out.print(frags[k]+"...");
			              //out.println("contents: " + doc.get("contents"));
			              //out.println("</p>");
			          } 
			          out.println("</p>");
			      }
			      reader.close();
		    	}
		    
	      Query query = parser.parse(text);           
	      
	      query = query.rewrite(reader);
			DoubleValuesSource boostByField = DoubleValuesSource.fromLongField("boost");
			FunctionScoreQuery modifiedQuery = new FunctionScoreQuery(query, boostByField);
	      TopDocs hits = searcher.search(modifiedQuery, 5);
	      out.println("<br>" + hits.totalHits + "total matching documents");
	      out.println("</p>");
	      int pr =hits.totalHits;
	     if(pr <= 0) {
	      try {
	    	  //if(hits.totalHits > 0) {
	  	SpellCheck sp = new SpellCheck();
	  	out.print("Did you mean: ");
	  	out.println(sp.spell(text));
	  	out.println("</p>");
	    	 // }
	      }
     	 finally {
     	 } 
	      }
	      Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:blue;\"><b>","</b></span>");
	      QueryScorer scorer = new QueryScorer(query);
	      Highlighter highlighter = new Highlighter(formatter, scorer);
	      Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 100);
	      highlighter.setTextFragmenter(fragmenter);
	      
	      for (int i = 0; i < hits.scoreDocs.length; i++) {
	          int id = hits.scoreDocs[i].doc;
	          Document doc = searcher.doc(id);
	          String title = doc.get("path");
	          out.println(title);
	          out.println("</p>");
	          String text_word = doc.get("contents");
	          TokenStream stream = TokenSources.getAnyTokenStream(reader, id, "contents", analyzer);
	          String[] frags = highlighter.getBestFragments(stream, text_word, 5);
	          for (int k=0;k<frags.length;k++)
	          {
	              //System.out.println(path);
	              //System.out.println(frag);
	              //out.println("Path: " + doc.get("path"));
	             // out.println("</p>");
	        	  out.print("Abstract: ");
	              out.print(frags[k]+"...");
	              //out.println("contents: " + doc.get("contents"));
	              //out.println("</p>");
	          } 
	          out.println("</p>");
	      }
	      reader.close();
	  }
}
