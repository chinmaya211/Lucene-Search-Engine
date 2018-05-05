package Lucene;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;


import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
//import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.FSDirectory;


/**
 * Servlet implementation class Searcher
 */
public class Searcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Searcher() {
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

	public Query getWildcardQuery (String field, String termStr) throws Exception
	{
	    String term = termStr;
	    TokenStream stream = null;
	    try
	    {
	        // we want only a single token and we don't want to lose special characters
	        stream = new KeywordTokenizer ();

	        stream = new LowerCaseFilter (stream);
	        stream = new ASCIIFoldingFilter (stream);

	        CharTermAttribute charTermAttribute = stream.addAttribute (CharTermAttribute.class);

	        stream.reset ();
	        while (stream.incrementToken ())
	        {
	            term = charTermAttribute.toString ();
	        }
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    return getWildcardQuery (field, term);
	}
	
	public void search(String text, HttpServletResponse response) throws Exception {
		String index = "C:\\Study_Mac\\Information_Retrieval_Systems\\citeseer2All\\citeseer2_index";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = parser.parse(text);
		query = query.rewrite(reader);
		//Term term = new Term("contents", text);
		//Query query = new WildcardQuery(term);
		UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
		TopDocs results = searcher.search(query, 1342, Sort.INDEXORDER);
		out.println("<br>" + results.totalHits + "total matching documents");
		//System.out.println(results);
		//System.out.println(results.totalHits + " total matching documents");
		for (int i = 0; i < 1342; i++) {
			Document doc = searcher.doc(results.scoreDocs[i].doc);
			//System.out.println(results.scoreDocs[i].doc);
			String path = doc.get("path");
			//System.out.println(doc.get("contents"));
			out.println("</p>");
			System.out.println((i + 1) + ". " + path);
			String title = doc.get("title");
			//String contents = doc.get("contents");
			//System.out.println("<p>");
			if (title != null) {
				out.println("Path: " + doc.get("path"));
				out.println("</p>");
				out.println("Title: " + doc.get("title"));
				//out.println("Contents: " + doc.get("contents"));
				//System.out.println("   Title: " + doc.get("title"));
			}
			
			/* String input = doc.get("contents");
			out.println(input);
			Pattern p = Pattern.compile("(\\bAbstract\\b)(.*?)(\\b.\\b)");
			Matcher m = p.matcher(input);
			List<String> matches = new ArrayList<String>();
			while (m.find()) {
				out.println("Title: " + matches.add(m.group())); */
			
			out.println("</p>");
		}
		reader.close();
	}
}
