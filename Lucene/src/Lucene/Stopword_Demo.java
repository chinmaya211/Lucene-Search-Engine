package Lucene;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Paths;
//import java.nio.file.Paths;
//import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.ReuseStrategy;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
//import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.TypeTokenFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
//import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
/**
 * Servlet implementation class Stopword
 */
public class Stopword_Demo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Analyzer baseAnalyzer;
	  public Stopword_Demo(ReuseStrategy baseAnalyzer) {
			//super(baseAnalyzer);
			// TODO Auto-generated constructor stub
		}
	
	  public void close() {
	      baseAnalyzer.close();
	      //super.close();
	  }
	  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Stopword_Demo() {
        super();
        // TODO Auto-generated constructor stub
    }

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

	
	protected Analyzer getWrappedAnalyzer(String fieldName)
	  {
	      return baseAnalyzer;
	  }
	
	protected TokenStreamComponents wrapComponents(String fieldName, TokenStreamComponents components)
	  {
	      TokenStream ts = components.getTokenStream();
	      Set<String> filteredTypes = new HashSet<>();
	      filteredTypes.add("learn");
	      TypeTokenFilter numberFilter = new TypeTokenFilter(ts, filteredTypes);

	      PorterStemFilter porterStem = new PorterStemFilter(numberFilter);
	      return new TokenStreamComponents(components.getTokenizer(), porterStem);
	  }
	
	
	public void search(String text, HttpServletResponse response) throws Exception {
		//String index = "C:\\Users\\gochh\\data\\citeseer2_index";
		String index = "C:\\Study_Mac\\Information_Retrieval_Systems\\citeseer2All\\citeseer2_index";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		@SuppressWarnings("resource")
		Analyzer analyzer = new StandardAnalyzer();
		Analyzer analyzer1 = new StandardAnalyzer();
		QueryParser parser = new QueryParser("contents", analyzer1);
		
		
		String input = text;
		TokenStream ts = analyzer.tokenStream("fieldName", new StringReader(input));
	      ts.reset();
	      	      
          String xyz = "";
	      while (ts.incrementToken()){
	          CharTermAttribute ca = ts.getAttribute(CharTermAttribute.class);
	          //out.println("</p>");
	          //out.println(ca.toString());
	          if (ca.toString() != null) {
	          xyz = xyz + " " + ca.toString();
	          }
	          //out.println(xyz);
	          out.println("</p>");
	      }
	      out.println("the search word is:" + xyz);
	      Query query = parser.parse(xyz);
			TopDocs results = searcher.search(query, 5);
			//out.println("<br>" + results.totalHits + "total matching documents");
			for (int i = 0; i < 5; i++) {
				Document doc = searcher.doc(results.scoreDocs[i].doc);
				//System.out.println(results.scoreDocs[i].doc);
				String path = doc.get("path");
				//System.out.println(doc.get("contents"));
				out.println("</p>");
				System.out.println((i + 1) + ". " + path);
				String title = doc.get("title");
				System.out.println("<p>");
				if (title != null) {
					out.println("Path: " + doc.get("path"));
					out.println("</p>");
					out.println("Title: " + doc.get("title"));
					//System.out.println("   Title: " + doc.get("title"));
				}
				out.println("</p>");
			}
			//analyzer.close();
	      analyzer1.close();
	  }
}
