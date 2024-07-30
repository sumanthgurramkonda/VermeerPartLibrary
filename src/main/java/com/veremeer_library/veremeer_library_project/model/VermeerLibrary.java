package com.veremeer_library.veremeer_library_project.model;

public class VermeerLibrary {

	private String libraryTitle;
	private String classTitle1;
	private String classTitle2;
	private String classTitle3;
	private String classTitle4;
	private String classTitle5;
	private String status;
	private String owner;
	private String attributeCount;
	private String reviewed;
	private String descriptionAbbreviation;
	private String comments;
	private String descriptionTemplate;
	private String exampleTitle;
	private String characterCount;
	private String descriptionTemplate2;
	
	public VermeerLibrary() {
		
	}

	public String getLibraryTitle() {
		return libraryTitle;
	}

	public void setLibraryTitle(String libraryTitle) {
		this.libraryTitle = libraryTitle;
	}

	public String getClassTitle1() {
		return classTitle1;
	}

	public void setClassTitle1(String classTitle1) {
		this.classTitle1 = classTitle1;
	}

	public String getClassTitle2() {
		return classTitle2;
	}

	public void setClassTitle2(String classTitle2) {
		this.classTitle2 = classTitle2;
	}

	public String getClassTitle3() {
		return classTitle3;
	}

	public void setClassTitle3(String classTitle3) {
		this.classTitle3 = classTitle3;
	}

	public String getClassTitle4() {
		return classTitle4;
	}

	public void setClassTitle4(String classTitle4) {
		this.classTitle4 = classTitle4;
	}

	public String getClassTitle5() {
		return classTitle5;
	}

	public void setClassTitle5(String classTitle5) {
		this.classTitle5 = classTitle5;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAttributeCount() {
		return attributeCount;
	}

	public void setAttributeCount(String attributeCount) {
		this.attributeCount = attributeCount;
	}

	public String getReviewed() {
		return reviewed;
	}

	public void setReviewed(String reviewed) {
		this.reviewed = reviewed;
	}

	public String getDescriptionAbbreviation() {
		return descriptionAbbreviation;
	}

	public void setDescriptionAbbreviation(String descriptionAbbreviation) {
		this.descriptionAbbreviation = descriptionAbbreviation;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDescriptionTemplate() {
		return descriptionTemplate;
	}

	public void setDescriptionTemplate(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate;
	}

	public String getExampleTitle() {
		return exampleTitle;
	}

	public void setExampleTitle(String exampleTitle) {
		this.exampleTitle = exampleTitle;
	}

	public String getCharacterCount() {
		return characterCount;
	}

	public void setCharacterCount(String characterCount) {
		this.characterCount = characterCount;
	}

	public String getDescriptionTemplate2() {
		return descriptionTemplate2;
	}

	public void setDescriptionTemplate2(String descriptionTemplate2) {
		this.descriptionTemplate2 = descriptionTemplate2;
	}

	@Override
	public String toString() {
		return "VermeerLibrary [libraryTitle=" + libraryTitle + ", classTitle1=" + classTitle1 + ", classTitle2="
				+ classTitle2 + ", classTitle3=" + classTitle3 + ", classTitle4=" + classTitle4 + ", classTitle5="
				+ classTitle5 + ", status=" + status + ", owner=" + owner + ", attributeCount=" + attributeCount
				+ ", reviewed=" + reviewed + ", descriptionAbbreviation=" + descriptionAbbreviation + ", comments="
				+ comments + ", descriptionTemplate=" + descriptionTemplate + ", exampleTitle=" + exampleTitle
				+ ", characterCount=" + characterCount + ", descriptionTemplate2=" + descriptionTemplate2 + "]";
	}

}
