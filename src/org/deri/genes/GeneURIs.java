package org.deri.genes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class GeneURIs {

	static BufferedWriter write = null;
	static FileWriter fstream = null;
	public String endpointURI = "http://cu.homologene.bio2rdf.org/sparql";
	
	public static String SELECT_GENE_URI_TEMPLATE = "" +
		"select ?uri {" +
		"?uri <http://bio2rdf.org/homologene_vocabulary:has_gene_symbol> \"%s\" ." +
		"}";
	
	public void getGeneURIs(String filePath) {
		BufferedReader br = null;
		
		createFile("data/geneURIs.csv");
		
		try {
 			String line;
 			String[] str;
 			String uri = "";
 			br = new BufferedReader(new FileReader(filePath));
 			
 			while ((line = br.readLine()) != null) {
 				if(!line.equals("")) {
 					uri = "";
 					str = line.split(",");
 					System.out.println("processing: " + str[0]);
 					uri = getGeneURI(str[0]);
 					if(!uri.equals("")) {
 						writeToFile(str[0] + "," + uri);
 					}
 				}
 			}
 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
		
	}
	
	public String getGeneURI(String term) {
		//http://pubmed.bio2rdf.org/sparql
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpointURI,String.format(SELECT_GENE_URI_TEMPLATE, term));
		ResultSet rs = qe.execSelect();
		while(rs.hasNext()) {
			QuerySolution soln = rs.nextSolution();
			//lstAuthors.add(soln.getResource("author").toString().substring(soln.getResource("author").toString().lastIndexOf("/")+1));
			return soln.getResource("uri").toString();
		}
		
		return "";
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
		GeneURIs obj = new GeneURIs();
		obj.getGeneURIs("data/stats.csv");
		//System.out.println(obj.getGeneURI("AKR1B1"));
	}
	
}
