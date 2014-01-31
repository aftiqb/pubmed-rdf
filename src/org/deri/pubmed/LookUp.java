package org.deri.pubmed;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.deri.pubmed.resources.Article;
import org.deri.pubmed.resources.Author;
import org.deri.pubmed.resources.Chemical;
import org.deri.pubmed.resources.Journal;
import org.deri.pubmed.resources.MeshHeading;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import banner.eval.LinkedTcgaBanner;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * 
 * @author aftabiqbal
 *
 */
public class LookUp {

	String pubmedURL="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi";
	public static String serialization = "N-TRIPLES";
	
	public Document getArticle(String id, String graphURI, String filePath) {
		return request(id, graphURI, filePath);
	}
	
	public void getDocument() {
		
	}
	
	public Document request(String id, String graphURI, String filePath) {
		InputStream is = getXMLResponse(id);
		
		try {
			if(is == null) {
				System.out.println("Null Input Stream...");
				return null;
			}
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.parse(is);
	        
	        return doc;
	        
		} catch(Exception ex) {
			System.out.println("Invalid XML stream for: " + id);
			System.out.println(ex.getMessage());
			return null;
		}
		
	}
	
	// parse content of XML to get details
	public ArrayList<Article> processXMLResponse(Document doc, String id, String graphURI, String filePath) {
		ArrayList<Article> lstArticle = new ArrayList<Article>();
		
		//Todo: there are certain articles with PubmedBookArticle tag. Write the code to handle this case.
		
		NodeList responseNodes = doc.getElementsByTagName("PubmedArticle");
        for (int i = 0; i < responseNodes.getLength(); i++) {
        	Element element = (Element)responseNodes.item(i);
        	// do processing here...
        	lstArticle.add(processArticleDetails(element));
        	
        }
		
        return lstArticle;
	}
	
	// parse content of an individual article from XML
	public Article processArticleDetails(Element element) {
		
		Article article = new Article();
		Journal jrnl = new Journal();
		Author author = new Author();
		MeshHeading heading = new MeshHeading();
		Chemical ch = new Chemical();
		ArrayList<Author> lstAuthor = new ArrayList<Author>();
		ArrayList<Chemical> lstChemical = new ArrayList<Chemical>();
		ArrayList<String> lstPublicationTypes = new ArrayList<String>();
		ArrayList<MeshHeading> lstHeading = new ArrayList<MeshHeading>();
		ArrayList<String> lstQualifiers = new ArrayList<String>();
		
		NodeList nl = null;
		Element el = null;
		if(element.getElementsByTagName("MedlineCitation").getLength() >= 1) {
			el = (Element)element.getElementsByTagName("MedlineCitation").item(0);
			article.setOwner(el.getAttribute("Owner"));
			article.setStatus(el.getAttribute("Status"));
		}
		
		if(element.getElementsByTagName("PMID").getLength() >= 1) {
			article.setId(element.getElementsByTagName("PMID").item(0).getTextContent());
			article.setPubmedIdentifier(element.getElementsByTagName("PMID").item(0).getTextContent());
		}
		
		if(element.getElementsByTagName("Article").getLength() >= 1) {
			el = (Element)element.getElementsByTagName("Article").item(0);
			article.setPublicationModel(el.getAttribute("PubModel"));
		}
		
		if(element.getElementsByTagName("Volume").getLength() >= 1)
			jrnl.setJournalVolume(element.getElementsByTagName("Volume").item(0).getTextContent());
		if(element.getElementsByTagName("Issue").getLength() >= 1)
			jrnl.setJournalIssue(element.getElementsByTagName("Issue").item(0).getTextContent());
		if(element.getElementsByTagName("Title").getLength() >= 1)
			jrnl.setJournalTitle(element.getElementsByTagName("Title").item(0).getTextContent());
		if(element.getElementsByTagName("ISOAbbreviation").getLength() >= 1)
			jrnl.setJournalAbbreviation(element.getElementsByTagName("ISOAbbreviation").item(0).getTextContent());
		
		article.setJrnl(jrnl);
		
		if(element.getElementsByTagName("ArticleTitle").getLength() >= 1)
			article.setLabel(element.getElementsByTagName("ArticleTitle").item(0).getTextContent());
		// Sometimes there are more than one abstracts (e.g., see article 23643085). implement the functionality to handle this case.
		if(element.getElementsByTagName("AbstractText").getLength() >=1)
			article.setArticleAbstract(element.getElementsByTagName("AbstractText").item(0).getTextContent());
		if(element.getElementsByTagName("Affiliation").getLength() >=1)
			article.setAffiliation(element.getElementsByTagName("Affiliation").item(0).getTextContent());
		
		
		nl = element.getElementsByTagName("Author");
		
		for (int i = 0; i < nl.getLength(); i++) {
			Element ele = (Element) nl.item(i);
			author = new Author();
			
			if(ele.getElementsByTagName("LastName").getLength() >=1)
				author.setLastName(ele.getElementsByTagName("LastName").item(0).getTextContent());
			if(ele.getElementsByTagName("ForeName").getLength() >=1)
				author.setForeName(ele.getElementsByTagName("ForeName").item(0).getTextContent());
			if(ele.getElementsByTagName("Initials").getLength() >=1)
				author.setInitial(ele.getElementsByTagName("Initials").item(0).getTextContent());
			
			lstAuthor.add(author);
		}
		
		article.setLstAuthor(lstAuthor);
		
		if(element.getElementsByTagName("Language").getLength() >=1)
			article.setLanguage(element.getElementsByTagName("Language").item(0).getTextContent());
		
		if(element.getElementsByTagName("PublicationType").getLength() >=1) {
			nl = element.getElementsByTagName("PublicationType");
			for (int i = 0; i < nl.getLength(); i++) {
				lstPublicationTypes.add(nl.item(i).getTextContent());
			}
			article.setLstPublicationType(lstPublicationTypes);
		}
		
		if(element.getElementsByTagName("Chemical").getLength() >=1) {
			nl = element.getElementsByTagName("Chemical");
			for (int i = 0; i < nl.getLength(); i++) {
				Element ele = (Element) nl.item(i);
				ch = new Chemical();
				
				if(ele.getElementsByTagName("RegistryNumber").getLength() >=1)
					ch.setRegistryNo(ele.getElementsByTagName("RegistryNumber").item(0).getTextContent());
				if(ele.getElementsByTagName("NameOfSubstance").getLength() >=1)
					ch.setName(ele.getElementsByTagName("NameOfSubstance").item(0).getTextContent());
				
				lstChemical.add(ch);
			}
			article.setLstChemical(lstChemical);
		}
		
		if(element.getElementsByTagName("MeshHeading").getLength() >=1) {
			nl = element.getElementsByTagName("MeshHeading");
			for (int i = 0; i < nl.getLength(); i++) {
				Element ele = (Element) nl.item(i);
				heading = new MeshHeading();
				if(ele.getElementsByTagName("DescriptorName").getLength() >=1)
					heading.setDescriptorName(ele.getElementsByTagName("DescriptorName").item(0).getTextContent());
				if(ele.getElementsByTagName("QualifierName").getLength() >=1) {
					lstQualifiers = new ArrayList<String>();
					NodeList qName = ele.getElementsByTagName("QualifierName");
					for(int j = 0; j < qName.getLength(); j++) {
						lstQualifiers.add(qName.item(j).getTextContent());
					}
					heading.setQualifierName(lstQualifiers);
				}
				lstHeading.add(heading);
			}
			article.setLstHeading(lstHeading);
		}
		
		
		return article;
	}
	
		
	public InputStream getXMLResponse(String id) {
		try {
			
			URL url;
			url = new URL(pubmedURL + "?db=pubmed&id=" + id + "&rettype=xml&retmode=text");
			
			URLConnection con = url.openConnection();
        	
        	return con.getInputStream();
			
		} catch (Exception e) {
			System.out.println("exception in getXMLResponse: " + e.getLocalizedMessage());
			return null;
        }
		
		
	}
	
	private static void usage() {
		System.out.println("usage: LookUp [parameters]");
		System.out.println();
		System.out.println("	-i id	Article ID (For example, 23977046. For multiple articles, use the following format: 23977046,23977047,2838570).");
		System.out.println("	(optional)-f format	RDF format for serialization (RDF/XML, TURTLE, N-TRIPLES). Default is TURTLE");
	}
	
	
	public static void main(String[] args) throws Exception {
		
		LookUp obj = new LookUp();
		CommandLineParser parser = new BasicParser( );
		Options options = new Options( );
		options.addOption("h", "help", false, "Print this usage information");
		options.addOption("i", "id", true, "Article ID (For example, 23977046. For multiple articles, use the following format: 23977046,23977047,2838570).");
		options.addOption("f", "format", true, "RDF format for serialization (RDF/XML, TURTLE, N-TRIPLES).");
		CommandLine commandLine = parser.parse( options, args );
		
		if( commandLine.hasOption('h') ) {
		    usage();
		    return;
		 }
		
		if(commandLine.hasOption('f'))
			serialization = commandLine.getOptionValue('f');
		
		obj.getArticle(commandLine.getOptionValue('i'), "http://tcga.deri.ie/graph/blca", "data/");
	}
}
