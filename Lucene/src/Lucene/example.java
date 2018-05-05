package Lucene;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
/**
 * Servlet implementation class Highlighter
 */
@WebServlet("/Highlighter")
public class example extends HttpServlet {
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
	}
    
	
	public void search(String text, HttpServletResponse response) throws Exception {
	  	String index = "C:\\Users\\gochh\\data\\citeseer2_index";
	  	//System.out.println(Paths.get(index));
	  	Directory dir = FSDirectory.open(Paths.get(index));
	  	IndexReader reader = DirectoryReader.open(dir);
	  	IndexSearcher searcher = new IndexSearcher(reader);
	      Analyzer analyzer = new StandardAnalyzer();
	      QueryParser parser = new QueryParser("contents", analyzer);
	      Query query = parser.parse("retrieval");
	      query = query.rewrite(reader);
	      TopDocs hits = searcher.search(query, 10);
	      System.out.println("totalHits:" + hits.totalHits);
	      Formatter formatter = new SimpleHTMLFormatter();
	      //System.out.println(htmlFormatter);
	      QueryScorer scorer = new QueryScorer(query);
	      Highlighter highlighter = new Highlighter(formatter, scorer);
	      Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);
	      highlighter.setTextFragmenter(fragmenter);
	      
	      for (int i = 0; i < hits.scoreDocs.length; i++) {
	          int id = hits.scoreDocs[i].doc;
	          Document doc = searcher.doc(id);
	          String path = doc.get("path");
	          String text1 = doc.get("contents");
	          TokenStream stream = TokenSources.getAnyTokenStream(reader, id, "contents", analyzer);
	          String[] frags = highlighter.getBestFragments(stream, text1, 10);
	          for (String frag : frags)
	          {
	              System.out.println(path);
	              System.out.println(frag);
	          }  
	          
	      }
	      reader.close();
	  }
}
