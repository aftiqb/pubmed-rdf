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
	public static String serialization = "N-TRIPLES";
	
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
	        System.out.println(count);
	        ArrayList<String> lstIDs = getIDs(count,days, graphURI, filePath, search);
	        
	        Publish publish = new Publish();
	        Model model = null;
	        
	        //String id = "24474151";
	        //String id = "24474393";
	        for(String id:lstIDs) {
	        	System.out.println("processing: " + id);
	        	Document article = obj.getArticle(id, graphURI, filePath);
	        	
	        	ArrayList<Article> lstArticle = obj.processXMLResponse(article, id, graphURI, filePath);
	        	for(Article art:lstArticle) {
	        		//ArrayList<String> lstDisease = publish.getAnnotations_Disease(art.getArticleAbstract(), diseaseBanner);
	        		//for(String disease:lstDisease)
	        		//System.out.println("**********************************************************");
	        		//System.out.println("publishing: " + art.getId());
	        		
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
	
	public static void main(String[] args) {
		Main obj = new Main();
		LinkedTcgaBanner diseaseBanner = new LinkedTcgaBanner("config/banner_NCBIDisease_DEV.xml");
		LinkedTcgaBanner geneBanner = new LinkedTcgaBanner("config/banner_UIMA_TEST.xml");
		
		obj.getLatestArticles(1, "", "data/test/", diseaseBanner, geneBanner);
	}
}
// 24474395, 24474195, 24472679, 24411092, 24411089, 24411087, 24398227, 24398226, 24394591, 24394385, 24393634
// 24393633, 24388700, 