package org.deri.pubmed;


import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.deri.datamodel.DataStoreModel;
import org.deri.pubmed.resources.Article;
import org.deri.pubmed.resources.Author;
import org.deri.pubmed.resources.Chemical;
import org.deri.pubmed.resources.Journal;
import org.deri.pubmed.resources.MeshHeading;
import org.deri.pubmed.resources.Utils;

import banner.eval.LinkedTcgaBanner;
import banner.types.Mention;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * @author aftabiqbal
 *
 */
public class Publish {

	String baseURI = "http://linked2safety.deri.org/pubmed/";
	
	public Model convertToRDF(Article article, String graphURI, LinkedTcgaBanner diseaseBanner, LinkedTcgaBanner geneBanner, DataStoreModel dsModel) {
		int count = 1;
		Model model = Utils.getModelProperties();
		Resource root = model.createResource(baseURI + article.getId());
		
		model.add(root,Utils.type,Utils.record);
		model.add(root,Utils.label,article.getLabel());
		
		// add the 'relatedTo' triple only if we know the graph to which this article belongs.
		if(!graphURI.equals(""))
			model.add(root,Utils.relatedTo,model.createResource(graphURI));
		
		model.add(root,Utils.status,article.getStatus());
		for(String type:article.getLstPublicationType())
			model.add(root,Utils.pubType,type);
		model.add(root,Utils.language,article.getLanguage());
		for(MeshHeading heading:article.getLstHeading()) {
			model.add(root,Utils.heading,model.createResource(baseURI + article.getId() + "/mesh_heading/" + count));
			count++;
		}
		count = 1;
		model.add(root,Utils.journal,model.createResource(baseURI + article.getId() + "/Journal"));
		for(Chemical ch:article.getLstChemical()) {
			model.add(root,Utils.chemical,model.createResource(baseURI + article.getId() + "/chemical/" + count));
			count++;
		}
		count = 1;
		model.add(root,Utils.identifier,"pubmed:"+article.getId());
		model.add(root,Utils.pubModel,article.getPublicationModel());
		for(Author author:article.getLstAuthor()) {
			model.add(root,Utils.author,model.createResource(baseURI + article.getId() + "/author/" + count));
			count++;
		}
		count = 1;
		model.add(root,Utils.affiliation,article.getAffiliation());
		model.add(root,Utils.owner,article.getOwner());
		model.add(root,Utils.articleAbstract,model.createResource(baseURI + article.getId() + "/abstract"));
		model.add(root,Utils.title,article.getLabel());
		
		for(String diseaseURI:getAnnotations_Disease(article.getArticleAbstract(), diseaseBanner, dsModel)) {
			model.add(root,Utils.relatedTo,model.createResource(diseaseURI));
		}
		
		for(String geneURI:getAnnotations_Genes(article.getArticleAbstract(), geneBanner, dsModel)) {
			model.add(root,Utils.relatedTo,model.createResource(geneURI));
		}
		
		model = describeHeadings(model, article);
		model = describeChemicals(model, article);
		model = describeAuthors(model, article);
		model = describeAbstract(model, article);
		model = describeJournal(model, article);
		
		return model;
	}
	
	public Model convertToRDF(Article article, String graphURI) {
		int count = 1;
		Model model = Utils.getModelProperties();
		Resource root = model.createResource(baseURI + article.getId());
		
		model.add(root,Utils.type,Utils.record);
		model.add(root,Utils.label,article.getLabel());
		
		// add the 'relatedTo' triple only if we know the graph to which this article belongs.
		if(!graphURI.equals(""))
			model.add(root,Utils.relatedTo,model.createResource(graphURI));
		
		model.add(root,Utils.status,article.getStatus());
		for(String type:article.getLstPublicationType())
			model.add(root,Utils.pubType,type);
		model.add(root,Utils.language,article.getLanguage());
		for(MeshHeading heading:article.getLstHeading()) {
			model.add(root,Utils.heading,model.createResource(baseURI + article.getId() + "/mesh_heading/" + count));
			count++;
		}
		count = 1;
		model.add(root,Utils.journal,model.createResource(baseURI + article.getId() + "/Journal"));
		for(Chemical ch:article.getLstChemical()) {
			model.add(root,Utils.chemical,model.createResource(baseURI + article.getId() + "/chemical/" + count));
			count++;
		}
		count = 1;
		model.add(root,Utils.identifier,"pubmed:"+article.getId());
		model.add(root,Utils.pubModel,article.getPublicationModel());
		for(Author author:article.getLstAuthor()) {
			model.add(root,Utils.author,model.createResource(baseURI + article.getId() + "/author/" + count));
			count++;
		}
		count = 1;
		model.add(root,Utils.affiliation,article.getAffiliation());
		model.add(root,Utils.owner,article.getOwner());
		model.add(root,Utils.articleAbstract,model.createResource(baseURI + article.getId() + "/abstract"));
		model.add(root,Utils.title,article.getLabel());
		
		model = describeHeadings(model, article);
		model = describeChemicals(model, article);
		model = describeAuthors(model, article);
		model = describeAbstract(model, article);
		model = describeJournal(model, article);
		
		return model;
	}
	
	
	public ArrayList<String> getAnnotations_Disease(String text, LinkedTcgaBanner diseaseBanner, DataStoreModel dsModel) {
		ArrayList<String> lstDiseaseURIs = new ArrayList<String>();
		Map<String, Set<Mention>>  map = diseaseBanner.getMentions(text);
        for(String sentence: map.keySet()) {
        	for(Mention m: map.get(sentence)) {
        		//System.out.println("> " + m.getEntityType() + " - "+ m.getText());
        		//System.out.println(m.getEntityType());
        		if(m.getEntityType().toString().trim().equals("Disease")) {
        			//System.out.println("yes");
        			// get disease URI from DiseaseOntology
        			String diseaseURI = dsModel.getDiseaseURI_RDFSLABEL(m.getText().toLowerCase());
        			if(!diseaseURI.equals("")) {
        				if(!lstDiseaseURIs.contains(diseaseURI))
        				lstDiseaseURIs.add(diseaseURI);
        			}
        			else {
        				diseaseURI = dsModel.getDiseaseURI_TCGALABEL(m.getText().toLowerCase());
        				if(!diseaseURI.equals("")) {
        					if(!lstDiseaseURIs.contains(diseaseURI))
        					lstDiseaseURIs.add(diseaseURI);
        				}
        				else {
        					diseaseURI = dsModel.getDiseaseURI_ACRONYM(m.getText().toLowerCase());
            				if(!diseaseURI.equals("")) {
            					if(!lstDiseaseURIs.contains(diseaseURI))
            					lstDiseaseURIs.add(diseaseURI);
            				}
            				else {
            					diseaseURI = dsModel.getDiseaseURI_SYNONYMS(m.getText().toLowerCase());
                				if(!diseaseURI.equals("")) {
                					if(!lstDiseaseURIs.contains(diseaseURI))
                					lstDiseaseURIs.add(diseaseURI);
                				}
            				}
        				}
        			}
        		}
        	}
        }
        // 24474393 , this article returns URI three times, check this case
        //System.out.println(lstDiseaseURIs.toString());
        return lstDiseaseURIs;
	}

	public ArrayList<String> getAnnotations_Genes(String text, LinkedTcgaBanner geneBanner, DataStoreModel dsModel) {
		ArrayList<String> lstGeneURIs = new ArrayList<String>();
		ArrayList<String> lstURIs = new ArrayList<String>();
		Map<String, Set<Mention>>  map = geneBanner.getMentions(text);
        for(String sentence: map.keySet()) {
        	for(Mention m: map.get(sentence)) {
        		System.out.println("> " + m.getEntityType() + " - "+ m.getText());
        		if(m.getEntityType().toString().trim().equals("GENE")) {
        			System.out.println("gene found ...");
        			//in lower case
        			lstURIs = new ArrayList<String>();
        			lstURIs = dsModel.getGeneURI_RDFSLABEL(m.getText().toLowerCase());
        			if(lstURIs.size() > 0) {
        				for(String geneURI:lstURIs) {
        					if(!lstGeneURIs.contains(geneURI))
        						lstGeneURIs.add(geneURI);
        				}
        			}
        			else {
        				lstURIs = new ArrayList<String>();
            			lstURIs = dsModel.getGeneURI_SYNONYMS(m.getText().toLowerCase());
            			for(String geneURI:lstURIs) {
        					if(!lstGeneURIs.contains(geneURI))
        						lstGeneURIs.add(geneURI);
        				}
        			}
        		}
        	}
        }
        System.out.println(lstGeneURIs.toString());
        return lstGeneURIs;
	}

	public Model describeJournal(Model model, Article article) {
		Journal jrnl = article.getJrnl();
		Resource root;
		root = model.createResource(baseURI + article.getId() + "/Journal");
		model.add(root,Utils.type,Utils.journalType);
		model.add(root,Utils.jrnlAbbreviation, jrnl.getJournalAbbreviation());
		model.add(root,Utils.jrnlIssue, jrnl.getJournalIssue());
		model.add(root,Utils.jrnlTitle, jrnl.getJournalTitle());
		model.add(root,Utils.jrnlVolume, jrnl.getJournalVolume());
		
		return model;
	}
	
	public Model describeAbstract(Model model, Article article) {
		Resource root;
		root = model.createResource(baseURI + article.getId() + "/abstract");
		model.add(root,Utils.type,Utils.Abstract);
		model.add(root,Utils.abstractText, article.getArticleAbstract());
		
		return model;
	}
	
	public Model describeHeadings(Model model, Article article) {
		int count = 1;
		Resource root;
		
		for(MeshHeading heading:article.getLstHeading()) {
			root = model.createResource(baseURI + article.getId() + "/mesh_heading/" + count);
			model.add(root,Utils.type,Utils.meshHeading);
			model.add(root,Utils.label, heading.getDescriptorName());
			model.add(root,Utils.descriptorName, heading.getDescriptorName());
			for(String s:heading.getQualifierName())
				model.add(root,Utils.qualifierName, s);
			
			count++;
		}
		return model;
	}
	
	public Model describeChemicals(Model model, Article article) {
		int count = 1;
		Resource root;
		for(Chemical ch:article.getLstChemical()) {
			root = model.createResource(baseURI + article.getId() + "/chemical/" + count);
			model.add(root,Utils.type,Utils.chemicalType);
			model.add(root,Utils.label,ch.getName());
			model.add(root,Utils.registry,ch.getRegistryNo());
			
			count++;
		}
		
		return model;
	}
	
	public Model describeAuthors(Model model, Article article) {
		int count = 1;
		Resource root;
		for(Author author:article.getLstAuthor()) {
			root = model.createResource(baseURI + article.getId() + "/author/" + count);
			model.add(root,Utils.type, Utils.authorType);
			model.add(root,Utils.foreName,author.getForeName());
			model.add(root,Utils.lastName,author.getLastName());
			model.add(root,Utils.initial,author.getInitial());
			
			count++;
		}
		
		return model;
	}
	
	public void publishRDFData(Model model, String fileName, String serialization, String filePath) {
		String fileExt = "";
		if(serialization.equals("N-TRIPLES"))
			fileExt = ".nt";
		else if(serialization.equals("TURTLE"))
			fileExt = ".ttl";
		else if(serialization.equals("RDF/XML"))
			fileExt = ".rdf";
		try {
			OutputStream output = new FileOutputStream(filePath + fileName + fileExt,false);
			model.write(output,serialization.toUpperCase());
			//model.write(System.out,serialization.toUpperCase());
		}catch(Exception e) {
	   		System.out.println("Error while publishing RDF file: " + e.getMessage());
	   	}
	}
	
}
