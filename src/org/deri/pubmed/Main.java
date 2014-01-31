package org.deri.pubmed;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.deri.datamodel.DataStoreModel;
import org.deri.pubmed.resources.Article;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hp.hpl.jena.rdf.model.Model;

import banner.eval.LinkedTcgaBanner;

/**
 * 
 * @author aftabiqbal
 *
 */
public class Main {
	String pubmedURL="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi";
	Integer days = 1;
	public static String serialization = "TURTLE";
	
	public void getLatestArticles(int days, String graphURI, String filePath, LinkedTcgaBanner diseaseBanner, LinkedTcgaBanner geneBanner) {
		LookUp obj = new LookUp();
		String query = "?db=pubmed&reldate=" + days + "&datetype=pdat";
		Search search = new Search();
		
		DataStoreModel dsModel = new DataStoreModel();
		dsModel.addRDFtoDataModel("mapping/TCGA_Disease_Ontology.ttl", "http://tcga.aksw.org/disease/", "TURTLE");
		dsModel.addRDFtoDataModel("mapping/TCGA_Gene_Ontology.ttl", "http://tcga.aksw.org/gene/", "TURTLE");
		
		
		InputStream is = search.getXMLResponse(query);
		
		try {
			if(is == null) {
				System.out.println("Null Input Stream...");
				
			}
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.parse(is);
	        
	        int count = search.getResultCounts(doc);
	        ArrayList<String> lstIDs = getIDs(count,days, graphURI, filePath, search);
	        
	        Publish publish = new Publish();
	        Model model = null;
	        
	        ////////////////////// for testing purpose only //////////////////////////
	        lstIDs = new ArrayList<String>();
//	        lstIDs.add("24407023");lstIDs.add("24300559");lstIDs.add("24463490");lstIDs.add("24436845");lstIDs.add("24396034");lstIDs.add("24475018");
//	        lstIDs.add("24463996");lstIDs.add("24452594");lstIDs.add("24440614");lstIDs.add("24403485");lstIDs.add("24375787");lstIDs.add("24374177");
//	        lstIDs.add("24350420");lstIDs.add("24325055");lstIDs.add("24324465");lstIDs.add("24313617");lstIDs.add("24313043");lstIDs.add("24309609");
//	        lstIDs.add("24305708");lstIDs.add("24296882");lstIDs.add("23233044");lstIDs.add("23314866");lstIDs.add("24102374");lstIDs.add("23709676");
//	        lstIDs.add("24177041");lstIDs.add("24002543");lstIDs.add("23811386");lstIDs.add("23777451");lstIDs.add("23666700");lstIDs.add("23464222");
//	        lstIDs.add("23055020");lstIDs.add("22998857");lstIDs.add("23588197");lstIDs.add("23598976");lstIDs.add("23707302");lstIDs.add("23846349");
//	        lstIDs.add("24218181");lstIDs.add("24225484");lstIDs.add("24236654");lstIDs.add("24240593");lstIDs.add("24280990");lstIDs.add("24296882");
//	        lstIDs.add("24305708");lstIDs.add("24309609");lstIDs.add("24313043");lstIDs.add("24394385");lstIDs.add("24411092");lstIDs.add("24393633");
//	        lstIDs.add("24411087");lstIDs.add("24388700");lstIDs.add("24411089");lstIDs.add("24472679");lstIDs.add("24474195");lstIDs.add("24474395");
//	        lstIDs.add("24394591");lstIDs.add("24398227");lstIDs.add("24398226");lstIDs.add("24393634");
	        //////////////////////////////////////////////////////////////////////////
	        
	        

	        for(String id:lstIDs) {
	        	System.out.println("processing: " + id);
	        	Document article = obj.getArticle(id, graphURI, filePath);
	        	
	        	ArrayList<Article> lstArticle = obj.processXMLResponse(article, id, graphURI, filePath);
	        	for(Article art:lstArticle) {
	        		model = publish.convertToRDF(art, graphURI, diseaseBanner, geneBanner, dsModel);
	        		publish.publishRDFData(model, id, serialization, filePath);
	        	}
	        	
	       }
	       
		} catch(Exception ex) {
			System.out.println("Error while processing article ...");
			System.out.println(ex.getMessage());
		}
	}

	public ArrayList<String> getIDs(int count, int days, String graphURI, String filePath, Search search) {
		
		ArrayList<String> lstIDs = new ArrayList<String>();
		String query = "?db=pubmed&retmax=" + count + "&reldate=" + days + "&datetype=pdat";
        InputStream is = search.getXMLResponse(query);
        
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
	        }
	        
		} catch(Exception ex) {
			System.out.println("Invalid XML stream ...");
			System.out.println(ex.getMessage());
			return null;
		}
		
		return lstIDs;
	}
	
	public static void main(String[] args) {
		Main obj = new Main();
		LinkedTcgaBanner diseaseBanner = new LinkedTcgaBanner("config/banner_NCBIDisease_DEV.xml");
		LinkedTcgaBanner geneBanner = new LinkedTcgaBanner("config/banner_UIMA_TEST.xml");
		
		obj.getLatestArticles(1, "", "data/", diseaseBanner, geneBanner);
	}
}
