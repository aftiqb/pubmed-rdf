package org.deri.pubmed.resources;

/**
 * 
 * @author aftabiqbal
 *
 */
public class Author {

	protected String foreName;
	protected String lastName;
	protected String initial;

	public Author(String foreName, String lastName, String initial) {
		super();
		this.foreName = foreName;
		this.lastName = lastName;
		this.initial = initial;
	}
	
	public Author() {
		// TODO Auto-generated constructor stub
		this.foreName = "";
		this.lastName = "";
		this.initial = "";
	}

	public String getForeName() {
		return foreName;
	}
	public void setForeName(String foreName) {
		this.foreName = foreName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	
	@Override
	public String toString() {
		return "Author [foreName=" + foreName + ", lastName=" + lastName
				+ ", initial=" + initial + "]";
	}



}
