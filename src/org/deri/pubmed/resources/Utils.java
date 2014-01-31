package org.deri.pubmed.resources;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * 
 * @author aftabiqbal
 *
 */
public class Utils {

	public static String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
	public static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static String dcterms = "http://purl.org/dc/terms/";
//	public static String foaf = "http://xmlns.com/foaf/0.1/";
	public static String pubmed = "http://bio2rdf.org/pubmed_vocabulary:";
	public static String skos = "http://www.w3.org/2008/05/skos#";
	
	public static Property type;
	public static Property label;
	public static Property status;
	public static Property pubType;
	public static Property language;
	public static Property heading;
	public static Property journal;
	public static Property chemical;
	public static Property identifier;
	public static Property pubModel;
	public static Property author;
	public static Property affiliation;
	public static Property owner;
	public static Property articleAbstract;
	public static Property title;
	public static Property record;
	public static Property meshHeading;
	public static Property descriptorName;
	public static Property qualifierName;
	public static Property chemicalType;
	public static Property registry;
	public static Property authorType;
	public static Property foreName;
	public static Property lastName;
	public static Property initial;
	public static Property Abstract;
	public static Property abstractText;
	public static Property journalType;
	public static Property jrnlIssue;
	public static Property jrnlVolume;
	public static Property jrnlTitle;
	public static Property jrnlAbbreviation;
	public static Property relatedTo;
//	public static Property diseaseURI;
	
	public static Model getModelProperties() {
		Model m = ModelFactory.createDefaultModel();
		
		type = m.createProperty(rdf + "type");
		label = m.createProperty(rdfs + "label");
		status = m.createProperty(pubmed + "status");
		pubType = m.createProperty(pubmed + "publication_type");
		language = m.createProperty(dcterms + "language");
		heading = m.createProperty(pubmed + "mesh_heading");
		journal = m.createProperty(pubmed + "journal");
		chemical = m.createProperty(pubmed + "chemical");
		identifier = m.createProperty(dcterms + "identifier");
		pubModel = m.createProperty(pubmed + "publication_model");
		author = m.createProperty(pubmed + "author");
		affiliation = m.createProperty(pubmed + "affiliation");
		owner = m.createProperty(pubmed + "owner");
		articleAbstract = m.createProperty(dcterms + "abstract");
		title = m.createProperty(dcterms + "title");
		record = m.createProperty(pubmed + "PubMedRecord");
		meshHeading = m.createProperty(pubmed + "MeshHeading");
		descriptorName = m.createProperty(pubmed + "mesh_descriptor_name");
		qualifierName = m.createProperty(pubmed + "mesh_qualifier_name");
		chemicalType = m.createProperty(pubmed + "Chemical");
		registry = m.createProperty(pubmed + "cas_registry_number");
		authorType = m.createProperty(pubmed + "Author");
		foreName = m.createProperty(pubmed + "fore_name");
		lastName = m.createProperty(pubmed + "last_name");
		initial = m.createProperty(pubmed + "initials");
		Abstract = m.createProperty(pubmed + "ArticleAbstract");
		abstractText = m.createProperty(pubmed + "abstract_text");
		journalType = m.createProperty(pubmed + "Journal");
		jrnlIssue = m.createProperty(pubmed + "journal_issue");
		jrnlAbbreviation = m.createProperty(pubmed + "journal_abbreviation");
		jrnlVolume = m.createProperty(pubmed + "journal_volume");
		jrnlTitle = m.createProperty(pubmed + "journal_title");
		relatedTo = m.createProperty(skos + "related");
//		diseaseURI = m.createProperty(skos + "related");
		
		m.setNsPrefix("rdf", rdf);
		m.setNsPrefix("rdfs", rdfs);
		m.setNsPrefix("pubmed", pubmed);
		m.setNsPrefix("dcterms", dcterms);
		m.setNsPrefix("skos", skos);
		
		return m;
	}
}
