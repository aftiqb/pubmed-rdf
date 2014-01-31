package org.deri.pubmed.resources;

/**
 * 
 * @author aftabiqbal
 *
 */
public class Journal {

	protected String journalAbbreviation;
	protected String journalVolume;
	protected String journalIssue;
	protected String journalTitle;

	public Journal(String journalAbbreviation, String journalVolume,
			String journalIssue, String journalTitle) {
		super();
		this.journalAbbreviation = journalAbbreviation;
		this.journalVolume = journalVolume;
		this.journalIssue = journalIssue;
		this.journalTitle = journalTitle;
	}
	
	public Journal() {
		// TODO Auto-generated constructor stub
		this.journalAbbreviation = "";
		this.journalVolume = "";
		this.journalIssue = "";
		this.journalTitle = "";
	}

	public String getJournalAbbreviation() {
		return journalAbbreviation;
	}
	public void setJournalAbbreviation(String journalAbbreviation) {
		this.journalAbbreviation = journalAbbreviation;
	}
	public String getJournalVolume() {
		return journalVolume;
	}
	public void setJournalVolume(String journalVolume) {
		this.journalVolume = journalVolume;
	}
	public String getJournalIssue() {
		return journalIssue;
	}
	public void setJournalIssue(String journalIssue) {
		this.journalIssue = journalIssue;
	}
	public String getJournalTitle() {
		return journalTitle;
	}
	public void setJournalTitle(String journalTitle) {
		this.journalTitle = journalTitle;
	}
	
	@Override
	public String toString() {
		return "Journal [journalAbbreviation=" + journalAbbreviation
				+ ", journalVolume=" + journalVolume + ", journalIssue="
				+ journalIssue + ", journalTitle=" + journalTitle + "]";
	}
	
	
}
