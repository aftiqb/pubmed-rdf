package org.deri.pubmed;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.deri.pubmed.resources.Article;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hp.hpl.jena.rdf.model.Model;

import banner.eval.LinkedTcgaBanner;

public class Search {

	String pubmedURL="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi";
	public static String serialization = "N-TRIPLES";
	static BufferedWriter write = null;
	static FileWriter fstream = null;
	
	public Element searchTerm(String term, String graphURI, String filePath) {
		
		LookUp obj = new LookUp();
		String query = "?db=pubmed&term=" + term;
		InputStream is = getXMLResponse(query);
		
		try {
			if(is == null) {
				System.out.println("Null Input Stream...");
				return null;
			}
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.parse(is);
	        
	        int count = getResultCounts(doc);
	        ArrayList<String> lstIDs = getIDs(count,term, graphURI, filePath);
	        
	        Publish publish = new Publish();
	        Model model = null;
	        
	        for(String id:lstIDs) {
	        	Document article = obj.getArticle(id, graphURI, filePath);
	        	ArrayList<Article> lstArticle = obj.processXMLResponse(article, id, graphURI, filePath);
	        	for(Article art:lstArticle) {
	        		model = publish.convertToRDF(art, graphURI);
	        		publish.publishRDFData(model, id, serialization, filePath);
	        	}
	        }
	        
		} catch(Exception ex) {
			System.out.println("Invalid XML stream ...");
			System.out.println(ex.getMessage());
			return null;
		}
		
		return null;
	}
	
	public ArrayList<String> getIDs(int count, String term, String graphURI, String filePath) {
		
		ArrayList<String> lstIDs = new ArrayList<String>();
		String query = "?db=pubmed&retmax=" + count + "&term=" + term;
        InputStream is = getXMLResponse(query);
        
        try {
			if(is == null) {
				System.out.println("Null Input Stream...");
				return null;
			}
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.parse(is);
	        
	        NodeList nl = doc.getElementsByTagName("Id");
	        
	        for (int i = 0; i < nl.getLength(); i++) {
	        	Element ele = (Element) nl.item(i);
	        	lstIDs.add(ele.getTextContent());
//	        	System.out.println("publishing article: " + ele.getTextContent());
//	        	obj.getArticle(ele.getTextContent(), graphURI, filePath, diseaseBanner);
	        }
	        
		} catch(Exception ex) {
			System.out.println("Invalid XML stream ...");
			System.out.println(ex.getMessage());
			return null;
		}
		
        //System.out.println(lstIDs.size());
		return lstIDs;
	}
	
	public int getResultCounts(Document doc) {
		NodeList responseNodes = doc.getElementsByTagName("Count");
		if(responseNodes.getLength() != 0)
			return Integer.parseInt(responseNodes.item(0).getTextContent());
		else 
			return 0;
	}
	
	public InputStream getXMLResponse(String query) {
		try {
			URL url;
			url = new URL(pubmedURL + query);
			URLConnection con = url.openConnection();
		
			return con.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
        }
		
		return null;
	}
	
	public void createFile(String filePath) {
		try {
			fstream = new FileWriter(filePath,false);
			write = new BufferedWriter(fstream);
		} catch(Exception e) {
			System.out.println("Error creating file");
		}
	}
	
	public void writeToFile(String line) {
		try {
	    	write.write(new String(line.getBytes(),"UTF-8"));
	    	write.newLine();
	    	write.flush();
	    }
	    catch (Exception e) {//Catch exception if any
	    	      System.err.println("Exception occured while writing data: " + e.getMessage());
	    }
	}
	
	public static void main(String[] args) {
		Search obj = new Search();
		//obj.searchTerm("blca", "http://tcga.deri.ie/graph/blca", "data/"); //blca, laml, hnsc, kirp, cesc, read, lgg, prad, lusc, skcm
		obj.searchTerm("lusc", "http://tcga.deri.ie/graph/lusc", "data/"); 
		//obj.searchTerm("ATXN1", "", "data/Gene-related/");
	}



}
