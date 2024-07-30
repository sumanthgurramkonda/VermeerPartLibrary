package com.veremeer_library.veremeer_library_project;

import java.util.List;

import com.veremeer_library.veremeer_library_project.model.Attributes;
import com.veremeer_library.veremeer_library_project.model.PartClassification;
import com.veremeer_library.veremeer_library_project.model.RawData;
import com.veremeer_library.veremeer_library_project.model.VermeerLibrary;
import com.veremeer_library.veremeer_library_project.process_csv.ReadCSV;
import com.veremeer_library.veremeer_library_project.process_csv.WriteCSV;
import com.veremeer_library.veremeer_library_project.service.ProcessVermeerLibrary;

public class App {
	
    public static void main( String[] args ) {
    	
    	String[] partClasificationParams = {
    		"libraryTitle", "classTitle1", "classTitle2", "classTitle3", "classTitle4", "classTitle5", 
    		"status", "owner", "attributeCount", "reviewed", "descriptionAbbreviation", "comments", "descriptionTemplate",
    		"exampleTitle", "characterCount","descriptionTemplate2"		
    	};
    	
    	final String fileName = "vermeer_Library.csv";
    	ReadCSV<VermeerLibrary> readCSV = new ReadCSV<>(fileName,VermeerLibrary.class,partClasificationParams);
    	List<VermeerLibrary> libraryList = readCSV.readFile(true);
    	String[] rawParam = {
        		"decription",
        		"partNumber"
        	};
    	
    	final String rawDataPath = "rawData3.csv";
    	ReadCSV<RawData> readRawData = new ReadCSV<>(rawDataPath, RawData.class, rawParam);
    	List<RawData> rawAttributesList = readRawData.readFile(false);

    	String[] attributeParams = {"classTitle", "Attibute", "descriptionAbbrevation", 
    			"classMatchesLibrary", "AttributeName","OrderForDescription",
    			"type", "dimensions", "prefferedUnit", "isMultivalue", "description", 
    			"defaultValue", "authorizedValues", "authorizedAbbrevation", 
    			"minimum", "maximum", "required"    
    	};
        final String attributePath = "attribute1.csv";
    	ReadCSV<Attributes> readCSV2 = new ReadCSV<>(attributePath, Attributes.class, attributeParams);
    	List<Attributes> attributesList = readCSV2.readFile(true);
    	List<PartClassification> partClassifications = ProcessVermeerLibrary.processPartClassificationData(libraryList, rawAttributesList, attributesList);
//    	List<PartClassification> partClassifications = ProcessData.processPartClassificationData(libraryList, rawAttributesList,attributesList);
    	String[] headers = {
    			"PartNumber", "PartType", "Description", "MaturityState", "ClassificationPath",	
    			"AttributeName1", "AttributeValue1","DescriptionValue1", "AttributeName2", "AttributeValue2",
    			"DescriptionValue2","AttributeName3", "AttributeValue3","DescriptionValue3",
    			"AttributeName4", "AttributeValue4","DescriptionValue4", 
    			"AttributeName5", "AttributeValue5","DescriptionValue5",
    			"AttributeName6", "AttributeValue6","DescriptionValue6",
    			"AttributeName7", "AttributeValue7","DescriptionValue7", 
    			"AttributeName8", "AttributeValue8","DescriptionValue8",
    			"AttributeName9", "AttributeValue9","DescriptionValue9",
    			"AttributeName10", "AttributeValue10","DescriptionValue10",
    			"AttributeName11", "AttributeValue11","DescriptionValue11",
    	};
    		WriteCSV writer = new WriteCSV("resultdata.csv");
        	writer.writeHeaders(headers);
        	writer.WriteList(partClassifications);
        	for(int i=0;i<3;i++)System.out.println();
        	System.out.println("                                     "
        			+ "-------------------------Data has been processed-------------------------");

    }
}









