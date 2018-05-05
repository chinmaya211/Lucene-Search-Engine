package Lucene;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
//import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Servlet implementation class Wildquery
 */
public class Wildcardquery_Demo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Wildcardquery_Demo() {
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


public void search(String text, HttpServletResponse response) throws Exception {
	String index = "C:\\Users\\gochh\\data\\citeseer2_index";
	//String index = "C:\\Study_Mac\\Information_Retrieval_Systems\\citeseer2All\\citeseer2_index";
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	Directory dir = FSDirectory.open(Paths.get(index));
	IndexReader reader = DirectoryReader.open(dir);
	IndexSearcher searcher = new IndexSearcher(reader);
	Analyzer analyzer = new StandardAnalyzer();
	Query query = new WildcardQuery(new Term("contents", text));
	TopDocs hits = searcher.search(query, 100, Sort.INDEXORDER);
	out.println("<br>" + hits.totalHits + "total matching documents");
	out.println("</p>");
     UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
     String[] fragments = highlighter.highlight("contents", query, hits);
     for (int k=0;k<fragments.length;k++)
     {  
    	 Document doc = searcher.doc(hits.scoreDocs[k].doc);
			String path = doc.get("path");
			System.out.println((k + 1) + ". " + path);
			    System.out.println("<p>");
			    out.println("  Path: " + doc.get("path"));   
			    out.println("</p>");
         out.println("Results: " + fragments[k]);
         out.println("</p>");
     }  
	reader.close();
}
}
	
	
	
	
	
	
	
	
	
	