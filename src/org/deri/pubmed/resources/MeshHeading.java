package org.deri.pubmed.resources;

import java.util.ArrayList;

/**
 * 
 * @author aftabiqbal
 *
 */
public class MeshHeading {

	protected String descriptorName;
	protected ArrayList<String> qualifierName;
	
	public MeshHeading(String descriptorName, ArrayList<String> qualifierName) {
		super();
		this.descriptorName = descriptorName;
		this.qualifierName = qualifierName;
	}
	
	public MeshHeading() {
		// TODO Auto-generated constructor stub
		this.descriptorName = "";
		this.qualifierName = new ArrayList<String>();
	}

	public String getDescriptorName() {
		return descriptorName;
	}
	public void setDescriptorName(String descriptorName) {
		this.descriptorName = descriptorName;
	}
	public ArrayList<String> getQualifierName() {
		return qualifierName;
	}
	public void setQualifierName(ArrayList<String> qualifierName) {
		this.qualifierName = qualifierName;
	}
	
	@Override
	public String toString() {
		return "MeshHeading [descriptorName=" + descriptorName
				+ ", qualifierName=" + qualifierName + "]";
	}

	
	
}
