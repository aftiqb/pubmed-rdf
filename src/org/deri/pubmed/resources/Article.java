package org.deri.pubmed.resources;

import java.util.ArrayList;

/**
 * 
 * @author aftabiqbal
 *
 */
public class Article {

	protected String id;
	protected String pubmedIdentifier;
	protected String label;
	protected String articleAbstract;
	protected String owner;
	protected String status;
	protected String affiliation;
	protected String publicationModel;
	protected String language;
	
	protected ArrayList<MeshHeading> lstHeading;
	protected ArrayList<Chemical> lstChemical;
	protected ArrayList<Author> lstAuthor;
	protected ArrayList<String> lstPublicationType;

	Journal jrnl;
	
	public Article(String id, String pubmedIdentifier, String label,
			String articleAbstract, String owner, String status,
			String affiliation, String publicationModel, String language,
			ArrayList<MeshHeading> lstHeading, ArrayList<Chemical> lstChemical,
			ArrayList<Author> lstAuthor, ArrayList<String> lstPublicationType, Journal jrnl) {
		super();
		this.id = id;
		this.pubmedIdentifier = pubmedIdentifier;
		this.label = label;
		this.articleAbstract = articleAbstract;
		this.owner = owner;
		this.status = status;
		this.affiliation = affiliation;
		this.publicationModel = publicationModel;
		this.language = language;
		this.lstHeading = lstHeading;
		this.lstChemical = lstChemical;
		this.lstAuthor = lstAuthor;
		this.lstPublicationType = lstPublicationType;
		this.jrnl = jrnl;
	}
	
	public Article() {
		// TODO Auto-generated constructor stub
		this.id = "";
		this.pubmedIdentifier = "";
		this.label = "";
		this.articleAbstract = "";
		this.owner = "";
		this.status = "";
		this.affiliation = "";
		this.publicationModel = "";
		this.language = "";
		this.lstHeading = new ArrayList<MeshHeading>();
		this.lstChemical = new ArrayList<Chemical>();
		this.lstAuthor = new ArrayList<Author>();
		this.lstPublicationType = new ArrayList<String>();
		this.jrnl = new Journal();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPubmedIdentifier() {
		return pubmedIdentifier;
	}
	public void setPubmedIdentifier(String pubmedIdentifier) {
		this.pubmedIdentifier = pubmedIdentifier;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getArticleAbstract() {
		return articleAbstract;
	}
	public void setArticleAbstract(String articleAbstract) {
		this.articleAbstract = articleAbstract;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public String getPublicationModel() {
		return publicationModel;
	}
	public void setPublicationModel(String publicationModel) {
		this.publicationModel = publicationModel;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public ArrayList<MeshHeading> getLstHeading() {
		return lstHeading;
	}
	public void setLstHeading(ArrayList<MeshHeading> lstHeading) {
		this.lstHeading = lstHeading;
	}
	public ArrayList<Chemical> getLstChemical() {
		return lstChemical;
	}
	public void setLstChemical(ArrayList<Chemical> lstChemical) {
		this.lstChemical = lstChemical;
	}
	public ArrayList<Author> getLstAuthor() {
		return lstAuthor;
	}
	public void setLstAuthor(ArrayList<Author> lstAuthor) {
		this.lstAuthor = lstAuthor;
	}
	public ArrayList<String> getLstPublicationType() {
		return lstPublicationType;
	}
	public void setLstPublicationType(ArrayList<String> publicationType) {
		this.lstPublicationType = publicationType;
	}
	public Journal getJrnl() {
		return jrnl;
	}
	public void setJrnl(Journal jrnl) {
		this.jrnl = jrnl;
	}
	
	@Override
	public String toString() {
		return "Article [id=" + id + ", pubmedIdentifier=" + pubmedIdentifier
				+ ", label=" + label + ", articleAbstract=" + articleAbstract
				+ ", owner=" + owner + ", status=" + status + ", affiliation="
				+ affiliation + ", publicationModel=" + publicationModel
				+ ", lstHeading=" + lstHeading + ", lstChemical=" + lstChemical
				+ ", lstAuthor=" + lstAuthor + ", publicationType="
				+ lstPublicationType + "]";
	}

}
