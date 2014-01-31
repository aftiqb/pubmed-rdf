package org.deri.pubmed.resources;

/**
 * 
 * @author aftabiqbal
 *
 */
public class Chemical {

	protected String name;
	protected String registryNo;

	public Chemical(String name, String registryNo) {
		super();
		this.name = name;
		this.registryNo = registryNo;
	}
	
	public Chemical() {
		// TODO Auto-generated constructor stub
		this.name = "";
		this.registryNo = "";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegistryNo() {
		return registryNo;
	}
	public void setRegistryNo(String registryNo) {
		this.registryNo = registryNo;
	}
	
	@Override
	public String toString() {
		return "Chemical [name=" + name + ", registryNo=" + registryNo + "]";
	}
	
	
}
