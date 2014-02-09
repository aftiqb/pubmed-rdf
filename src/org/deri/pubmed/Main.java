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
//	        lstIDs = new ArrayList<String>();
//	        lstIDs.add("23966394");lstIDs.add("23675467");lstIDs.add("23178488");lstIDs.add("19472918");lstIDs.add("18275850");lstIDs.add("24377578");
//	        lstIDs.add("23482336");lstIDs.add("23317270");lstIDs.add("21609483");lstIDs.add("24455749");lstIDs.add("23692254");lstIDs.add("23362149");
//	        lstIDs.add("23211419");lstIDs.add("22136433");lstIDs.add("22136433");lstIDs.add("24496626");lstIDs.add("24400073");lstIDs.add("24396288");
//	        lstIDs.add("22998857");lstIDs.add("24376795");lstIDs.add("24321550");lstIDs.add("24492705");lstIDs.add("23709676");lstIDs.add("23055020");
//	        lstIDs.add("22811704");lstIDs.add("22264718");lstIDs.add("21251473");lstIDs.add("24102374");lstIDs.add("23314866");lstIDs.add("22918816");
//	        lstIDs.add("22723265");lstIDs.add("21995959");lstIDs.add("19466120");lstIDs.add("19327015");lstIDs.add("18813745");lstIDs.add("24463490");
//	        lstIDs.add("24396034");lstIDs.add("24436845");lstIDs.add("24202301");lstIDs.add("24141111");lstIDs.add("24114817");lstIDs.add("23994163");
//	        lstIDs.add("21631174");lstIDs.add("21401713");lstIDs.add("19396560");lstIDs.add("9986909");lstIDs.add("9927476");lstIDs.add("24501384");
//	        lstIDs.add("24475018");lstIDs.add("24480927");lstIDs.add("24463996");lstIDs.add("24452594");lstIDs.add("24440614");lstIDs.add("24305708");
	        
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
