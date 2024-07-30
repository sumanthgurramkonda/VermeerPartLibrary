package com.veremeer_library.veremeer_library_project.process_csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import com.veremeer_library.veremeer_library_project.model.AttributeValues;
import com.veremeer_library.veremeer_library_project.model.PartClassification;

public class WriteCSV<T> {
	
	private final String path;
	private final String relativePath ="src/resources/";
	private CSVWriter writer;

	public WriteCSV(String path) {
		this.path = relativePath+path;
		try {
			writer = new CSVWriter(new FileWriter(new File(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeHeaders(String[] headers) {
		writeNext(headers);
	}
	public void WriteList(List<PartClassification> partClassifications) {
		
//		for(PartClassification partClassification: partClassifications) {
		for(int i=0;i<partClassifications.size();i++) {
			PartClassification partClassification = partClassifications.get(i);
			String classifiactionPath = partClassification.getClassificationPath();
			if(classifiactionPath==null)classifiactionPath="";
			String result = partClassification.getPartNumber()+"%"+partClassification.getPartType()+"%"
					       +partClassification.getDescription()+"%"+""+"%"
					       +classifiactionPath;
			List<AttributeValues> attributeValues = partClassification.getAttributeValues();
			if(attributeValues!=null) {                          
				for(AttributeValues attributeValue : attributeValues){
					result += "%"+attributeValue.getAttribute()+"%"+attributeValue.getAttributeValue()+"%"
							+attributeValue.getDescriptionValue();
				}
			}
			String[] res = result.split("%");
//			System.out.print(i+"  [    ");
//			for(String s : res) {
//				System.out.print(s+" , ");
//			}
//			System.out.println("    ]");
			writeNext(res);
			result="";
		}
	}
	
	public void writeNext(String[] data) {
		try {
			if(writer!=null) {
				writer.writeNext(data, true);
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
