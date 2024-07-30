package com.veremeer_library.veremeer_library_project.model;

public class Attributes {
	
	private String classTitle;
	private String Attibute;
	private String descriptionAbbrevation;
	private String classMatchesLibrary;
	private String AttributeName;
	private String OrderForDescription;
	private String type;
	private String dimensions;
	private String prefferedUnit;
	private String isMultivalue;
	private String description;
	private String defaultValue;
	private String authorizedValues;
	private String authorizedAbbrevation;
	private String minimum;
	private String maximum;
	private String required;
	
	
	public Attributes() {
		super();
	}
	public Attributes(String classTitle, String attibute, String descriptionAbbrevation, String classMatchesLibrary,
			String attributeName, String orderForDescription, String type, String dimensions, String prefferedUnit,
			String isMultivalue, String description, String defaultValue, String authorizedValues,
			String authorizedAbbrevation, String minimum, String maximum, String required) {
		super();
		this.classTitle = classTitle;
		Attibute = attibute;
		this.descriptionAbbrevation = descriptionAbbrevation;
		this.classMatchesLibrary = classMatchesLibrary;
		AttributeName = attributeName;
		OrderForDescription = orderForDescription;
		this.type = type;
		this.dimensions = dimensions;
		this.prefferedUnit = prefferedUnit;
		this.isMultivalue = isMultivalue;
		this.description = description;
		this.defaultValue = defaultValue;
		this.authorizedValues = authorizedValues;
		this.authorizedAbbrevation = authorizedAbbrevation;
		this.minimum = minimum;
		this.maximum = maximum;
		this.required = required;
	}
	
	
	public String getClassTitle() {
		return classTitle;
	}
	public void setClassTitle(String classTitle) {
		this.classTitle = classTitle;
	}
	public String getAttibute() {
		return Attibute;
	}
	public void setAttibute(String attibute) {
		Attibute = attibute;
	}
	public String getDescriptionAbbrevation() {
		return descriptionAbbrevation;
	}
	public void setDescriptionAbbrevation(String descriptionAbbrevation) {
		this.descriptionAbbrevation = descriptionAbbrevation;
	}
	public String getClassMatchesLibrary() {
		return classMatchesLibrary;
	}
	public void setClassMatchesLibrary(String classMatchesLibrary) {
		this.classMatchesLibrary = classMatchesLibrary;
	}
	public String getAttributeName() {
		return AttributeName;
	}
	public void setAttributeName(String attributeName) {
		AttributeName = attributeName;
	}
	public String getOrderForDescription() {
		return OrderForDescription;
	}
	public void setOrderForDescription(String orderForDescription) {
		OrderForDescription = orderForDescription;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	public String getPrefferedUnit() {
		return prefferedUnit;
	}
	public void setPrefferedUnit(String prefferedUnit) {
		this.prefferedUnit = prefferedUnit;
	}
	public String getIsMultivalue() {
		return isMultivalue;
	}
	public void setIsMultivalue(String isMultivalue) {
		this.isMultivalue = isMultivalue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getAuthorizedValues() {
		return authorizedValues;
	}
	public void setAuthorizedValues(String authorizedValues) {
		this.authorizedValues = authorizedValues;
	}
	public String getAuthorizedAbbrevation() {
		return authorizedAbbrevation;
	}
	public void setAuthorizedAbbrevation(String authorizedAbbrevation) {
		this.authorizedAbbrevation = authorizedAbbrevation;
	}
	public String getMinimum() {
		return minimum;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	public String getMaximum() {
		return maximum;
	}
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	@Override
	public String toString() {
		return "Attributes [classTitle=" + classTitle + ", Attibute=" + Attibute + ", descriptionAbbrevation="
				+ descriptionAbbrevation + ", classMatchesLibrary=" + classMatchesLibrary + ", AttributeName="
				+ AttributeName + ", OrderForDescription=" + OrderForDescription + ", type=" + type + ", dimensions="
				+ dimensions + ", prefferedUnit=" + prefferedUnit + ", isMultivalue=" + isMultivalue + ", description="
				+ description + ", defaultValue=" + defaultValue + ", authorizedValues=" + authorizedValues
				+ ", authorizedAbbrevation=" + authorizedAbbrevation + ", minimum=" + minimum + ", maximum=" + maximum
				+ ", required=" + required + "]";
	}
	
	
}
