package com.veremeer_library.veremeer_library_project.model;

public class AttributeValues {
	
	private String order;
	private String partTpe;
	private String attribute;
	private String attributeValue;
	private String descriptionValue;
	private String authAbrevations;
	private String authValues;
	
	
	public String getPartTpe() {
		return partTpe;
	}

	public void setPartTpe(String partTpe) {
		this.partTpe = partTpe;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public AttributeValues() {
		super();
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDescriptionValue() {
		return descriptionValue;
	}

	public void setDescriptionValue(String descriptionValue) {
		this.descriptionValue = descriptionValue;
	}

	public String getAuthAbrevations() {
		return authAbrevations;
	}

	public void setAuthAbrevations(String authAbrevations) {
		this.authAbrevations = authAbrevations;
	}

	public String getAuthValues() {
		return authValues;
	}

	public void setAuthValues(String authValues) {
		this.authValues = authValues;
	}

	@Override
	public String toString() {
		return "AttributeValues [order=" + order + ", partTpe=" + partTpe + ", attribute=" + attribute
				+ ", attributeValue=" + attributeValue + ", descriptionValue=" + descriptionValue + ", authAbrevations="
				+ authAbrevations + ", authValues=" + authValues + "]";
	}

}
