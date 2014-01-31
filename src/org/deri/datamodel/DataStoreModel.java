package org.deri.datamodel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.openrdf.OpenRDFException;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

public class DataStoreModel {

	Repository _repository=null;
	RepositoryConnection con = null;
	DataStoreModel dsModel;
	
	public static String SELECT_Gene_RDFS_LABEL_URI_TEMPLATE = "" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX tcga:  <http://tcga.deri.ie/schema/> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"SELECT distinct ?uri " +
		"WHERE { " +
		"?s a tcga:gene ." +
		"?s rdfs:label ?term ." +
		"?s tcga:uri ?uri ." +
		"FILTER (lcase(str(?term)) = \"%s\")" +
		"}";

	public static String SELECT_Gene_SYNONYMS_URI_TEMPLATE = "" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX tcga:  <http://tcga.deri.ie/schema/> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"SELECT distinct ?uri " +
		"WHERE { " +
		"?s a tcga:gene ." +
		"?s tcga:synonyms ?term ." +
		"?s tcga:uri ?uri ." +
		"FILTER (lcase(str(?term)) = \"%s\")" +
		"}";

	public static String SELECT_Disease_RDFS_LABEL_URI_TEMPLATE = "" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX tcga:  <http://tcga.deri.ie/schema/> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"SELECT distinct ?uri " +
		"WHERE { " +
		"?s a tcga:disease ." +
		"?s rdfs:label ?term ." +
		"?s tcga:ref ?uri ." +
		"FILTER (lcase(str(?term)) = \"%s\")" +
		"}";

	public static String SELECT_Disease_TCGA_LABEL_URI_TEMPLATE = "" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX tcga:  <http://tcga.deri.ie/schema/> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"SELECT distinct ?uri " +
		"WHERE { " +
		"?s a tcga:disease ." +
		"?s tcga:label ?term ." +
		"?s tcga:ref ?uri ." +
		"FILTER (lcase(str(?term)) = \"%s\")" +
		"}";
	
	public static String SELECT_Disease_ACRONYM_URI_TEMPLATE = "" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX tcga:  <http://tcga.deri.ie/schema/> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"SELECT distinct ?uri " +
		"WHERE { " +
		"?s a tcga:disease ." +
		"?s tcga:acronym ?term ." +
		"?s tcga:ref ?uri ." +
		"FILTER (lcase(str(?term)) = \"%s\")" +
		"}";
	
	public static String SELECT_Disease_Synonyms_URI_TEMPLATE = "" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX tcga:  <http://tcga.deri.ie/schema/> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"SELECT distinct ?uri " +
		"WHERE { " +
		"?s a tcga:disease ." +
		"?s tcga:synonyms ?term ." +
		"?s tcga:ref ?uri ." +
		"FILTER (lcase(str(?term)) = \"%s\")" +
		"}";

	public DataStoreModel() {
		
		_repository = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		
        try {
            _repository.initialize();
            con = _repository.getConnection();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
        }
        
	}
	
	public void shutdownRepository() {
        try {
        	con.close();
            _repository.shutDown();
            
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
        }
		
	}
	
	public void addRDFtoDataModel(String filePath, String baseURI, String format) {
		File file = new File(filePath);
		try {
			   if(format == "RDF/XML") 
				   con.add(file, baseURI, RDFFormat.RDFXML);
			   else if(format == "TURTLE")
				   con.add(file, baseURI, RDFFormat.TURTLE);
		}
		catch (OpenRDFException e) {
			System.out.println("Open RDF Exception :" + filePath + e.getMessage() );
		   // handle exception
		}
		catch (IOException e) {
			System.out.println("ERROR :: File IO Exception --> " + filePath);
			System.out.println(e.getMessage());
		   // handle io exception
		}
	}
	
	
	public String getDiseaseURI_RDFSLABEL(String term) {
		try {
			   RepositoryConnection con = _repository.getConnection();
			   try {
				 BindingSet bindingSet;

				 TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, String.format(SELECT_Disease_RDFS_LABEL_URI_TEMPLATE,term));
				 TupleQueryResult result = tupleQuery.evaluate();
			   	 if(result.hasNext()) {
			   		bindingSet = result.next();
			   		return bindingSet.getValue("uri").toString();
			   	 }
			   	 result.close();				   		
			   }
			   finally {
				   con.close();
			   }
		}
		catch (OpenRDFException e) {
		  // handle exception
			System.out.println("exception:" + e.getLocalizedMessage());
		}
		return "";
	}

	public String getDiseaseURI_TCGALABEL(String term) {
		try {
			   RepositoryConnection con = _repository.getConnection();
			   try {
				 BindingSet bindingSet;

				 TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, String.format(SELECT_Disease_TCGA_LABEL_URI_TEMPLATE,term));
				 TupleQueryResult result = tupleQuery.evaluate();
			   	 if(result.hasNext()) {
			   		bindingSet = result.next();
			   		return bindingSet.getValue("uri").toString();
			   	 }
			   	 result.close();				   		
			   }
			   finally {
				   con.close();
			   }
		}
		catch (OpenRDFException e) {
		  // handle exception
			System.out.println("exception:" + e.getLocalizedMessage());
		}
		return "";
	}

	public String getDiseaseURI_ACRONYM(String term) {
		try {
			   RepositoryConnection con = _repository.getConnection();
			   try {
				 BindingSet bindingSet;

				 TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, String.format(SELECT_Disease_ACRONYM_URI_TEMPLATE,term));
				 TupleQueryResult result = tupleQuery.evaluate();
			   	 if(result.hasNext()) {
			   		bindingSet = result.next();
			   		return bindingSet.getValue("uri").toString();
			   	 }
			   	 result.close();				   		
			   }
			   finally {
				   con.close();
			   }
		}
		catch (OpenRDFException e) {
		  // handle exception
			System.out.println("exception:" + e.getLocalizedMessage());
		}
		return "";
	}

	public String getDiseaseURI_SYNONYMS(String term) {
		
		try {
			   RepositoryConnection con = _repository.getConnection();
			   try {
				 BindingSet bindingSet;

				 TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, String.format(SELECT_Disease_Synonyms_URI_TEMPLATE,term));
				 TupleQueryResult result = tupleQuery.evaluate();
			   	 if(result.hasNext()) {
			   		bindingSet = result.next();
			   		return bindingSet.getValue("uri").toString();
			   	 }
			   	 result.close();				   		
			   }
			   finally {
				   con.close();
			   }
		}
		catch (OpenRDFException e) {
		  // handle exception
			System.out.println("exception:" + e.getLocalizedMessage());
		}
		return "";
	}

	public ArrayList<String> getGeneURI_RDFSLABEL(String term) {
		ArrayList<String> lstGeneURIs = new ArrayList<String>();
		try {
			   RepositoryConnection con = _repository.getConnection();
			   try {
				 BindingSet bindingSet;

				 TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, String.format(SELECT_Gene_RDFS_LABEL_URI_TEMPLATE,term));
				 TupleQueryResult result = tupleQuery.evaluate();
				 while(result.hasNext()) {
			   		bindingSet = result.next();
			   		if(!lstGeneURIs.contains(bindingSet.getValue("uri").toString()) & !bindingSet.getValue("uri").toString().trim().equals(""))
			   			lstGeneURIs.add(bindingSet.getValue("uri").toString());
			   	 }
			   	 result.close();				   		
			   }
			   finally {
				   con.close();
			   }
		}
		catch (OpenRDFException e) {
		  // handle exception
			System.out.println("exception:" + e.getLocalizedMessage());
		}
		return lstGeneURIs;
	}
	
	public ArrayList<String> getGeneURI_SYNONYMS(String term) {
		ArrayList<String> lstGeneURIs = new ArrayList<String>();
		try {
			   RepositoryConnection con = _repository.getConnection();
			   try {
				 BindingSet bindingSet;

				 TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, String.format(SELECT_Gene_SYNONYMS_URI_TEMPLATE,term));
				 TupleQueryResult result = tupleQuery.evaluate();
				 while(result.hasNext()) {
			   		bindingSet = result.next();
			   		if(!lstGeneURIs.contains(bindingSet.getValue("uri").toString()) & !bindingSet.getValue("uri").toString().trim().equals(""))
			   			lstGeneURIs.add(bindingSet.getValue("uri").toString());
			   	 }
			   	 result.close();				   		
			   }
			   finally {
				   con.close();
			   }
		}
		catch (OpenRDFException e) {
		  // handle exception
			System.out.println("exception:" + e.getLocalizedMessage());
		}
		return lstGeneURIs;
	}
	
	public static void main(String[] args) {
		DataStoreModel dsModel = new DataStoreModel();
		dsModel.addRDFtoDataModel("mapping/TCGA_Disease_Ontology.ttl", "http://tcga.aksw.org/disease/", "TURTLE");
		System.out.println("result: " + dsModel.getDiseaseURI_ACRONYM("esca"));
		
	}
	
}
