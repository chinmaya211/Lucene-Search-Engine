package Lucene;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.function.FunctionScoreQuery;
//import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Servlet implementation class UnifyHighlighter
 */
public class Unifyhighlighter_Demo extends HttpServlet {
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

	public void search(String text, HttpServletResponse response) throws Exception {
		String index = "C:\\Study_Mac\\Information_Retrieval_Systems\\citeseer2All\\sigmod_index";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Directory dir = FSDirectory.open(Paths.get(index));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
	    Analyzer analyzer = new StandardAnalyzer();
	    QueryParser parser = new QueryParser("topic_title", analyzer);
	    Query query = parser.parse(text);
	    

	    query = query.rewrite(reader);
		DoubleValuesSource boostByField = DoubleValuesSource.fromLongField("boost");
		FunctionScoreQuery modifiedQuery = new FunctionScoreQuery(query, boostByField);
		
	      TopDocs hits = searcher.search(modifiedQuery, 3);
	      out.println("<br>" + hits.totalHits + " total matching documents");
	      out.println("</p>");
		
		Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:blue;\"><b>","</b></span>");
	      QueryScorer scorer = new QueryScorer(query);
	      Highlighter highlighter = new Highlighter(formatter, scorer);
	      Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 100);
	      highlighter.setTextFragmenter(fragmenter);
	      

	      
	      for (int i = 0; i < hits.scoreDocs.length; i++) {
	          int id = hits.scoreDocs[i].doc;
	          Document doc = searcher.doc(id);
	          String title = doc.get("topic_title");
	          String node = doc.get("node");
	          out.println("Node: " + node);
	          out.println("</p>");
	          out.println("Title: " + title);
	          @SuppressWarnings("deprecation")	          
			TokenStream stream = TokenSources.getAnyTokenStream(reader, id, "topic_title", analyzer);
	          String[] frags = highlighter.getBestFragments(stream, title, 3);
	          out.print(frags.length);
	          for (int k=0;k<frags.length;k++)
	          {
	        	  out.print("Title: ");
	              out.print(frags[k]+"...");
	          } 
	          out.println("</p>");
	      }
	    reader.close();   
}

}