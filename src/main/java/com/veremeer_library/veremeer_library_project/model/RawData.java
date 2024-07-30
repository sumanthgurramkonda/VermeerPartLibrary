package com.veremeer_library.veremeer_library_project.model;

public class RawData {
	
	private String decription;
	private String partNumber;
	
	public RawData() {
	
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	@Override
	public String toString() {
		return "RawData [decription=" + decription + ", partNumber=" + partNumber + "]";
	}
	
	

}
