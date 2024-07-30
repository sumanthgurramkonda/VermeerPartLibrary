package com.veremeer_library.veremeer_library_project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.veremeer_library.veremeer_library_project.model.AttributeValues;
import com.veremeer_library.veremeer_library_project.model.Attributes;
import com.veremeer_library.veremeer_library_project.model.PartClassification;
import com.veremeer_library.veremeer_library_project.model.RawData;
import com.veremeer_library.veremeer_library_project.model.VermeerLibrary;

public class ProcessVermeerLibrary {
	
	public static Map<String, PartClassification> partClassificationMap = new LinkedHashMap<String, PartClassification>();
	public static Map<String, List<AttributeValues>> attributeValuesMap = new HashMap<>();
	
	public static List<PartClassification> processPartClassificationData(List<VermeerLibrary> libraryData, 
			List<RawData> rawData, List<Attributes> attributes){
		rawData = filterRawData(rawData);
		setPartClassificationMap(rawData,attributes);
		setClassificationPath(libraryData,attributes);
		setAttributes(attributes);
		setAttributeValues();
		return getPartClassifications();
	}
	private static List<RawData> filterRawData(List<RawData> rawData) {
		
		return rawData.stream().filter(data->{
				String partNumber = data.getPartNumber();
			    for(int i=0;i<partNumber.length();i++) {
			    	char c= partNumber.charAt(i);
			    	if(!(c>='0' && c<='9')) return false ;
			    }
			    return true;
			}).toList();
		}
	
	private static void setPartClassificationMap(List<RawData> rawData,List<Attributes> attributes) {

		Set<String> classTypes = new LinkedHashSet<String>();
		
		attributes.forEach(attribute->{
			String attb = attribute.getClassTitle().trim().toUpperCase().split(",")[0].trim();
			classTypes.add(attb);
		});
		
		rawData.forEach(data->{
			String partNumber = data.getPartNumber().trim();
			PartClassification partClassification = new PartClassification();
			partClassification.setPartNumber(partNumber);
			partClassification.setDescription(data.getDecription());
			String type =null;
				String types[] = data.getDecription().trim().split("-");
				if(types.length>1) {
					type = types[0].toUpperCase().trim();
					if(type.equalsIgnoreCase("WELDRING")){
						type = "WELD RING";
					}
					if(classTypes.contains(type)) {	
						partClassification.setPartType(type);
					}
				}
			    else {
				    partClassification.setPartType(data.getDecription().trim().toUpperCase());
			    }
			partClassificationMap.put(partNumber,partClassification);
		});
		
	}
	
	public static void setClassificationPath(List<VermeerLibrary> libraryList, List<Attributes> attributes) {
			
			partClassificationMap.forEach((key,val)->{
				String[] descriptions = val.getDescription().toLowerCase().trim().split("-");
				if(descriptions!=null && descriptions.length>1) {					
					String type = descriptions[0].trim(); 
					libraryList.forEach(data->{
						String[] arr3 = data.getClassTitle3().toLowerCase().split(",");
						String[] arr2 = data.getClassTitle2().toLowerCase().split(",");
						String[] arr1 = data.getClassTitle1().toLowerCase().split(",");
						int returnValue = 0;
					    returnValue	= setPartClassificationData(arr3, type, key, 3, data);
					    if(returnValue==0)
					    returnValue = setPartClassificationData(arr2, type, key, 2, data);
					    if(returnValue==0)
					    returnValue = setPartClassificationData(arr1, type, key, 1, data);
					});
				}
			});
	 }
	
	private static int setPartClassificationData(String[] classes, String type, String key,int classNum,VermeerLibrary library) {
			
		String libraryTitle = library.getLibraryTitle().trim();
		String classificationPath = "";
		int returnValue = 0;
		if(type.equalsIgnoreCase("WELDRING")){
			type = "weld ring";
		}
		for(String className : classes) {
			if(className.toLowerCase().equalsIgnoreCase(type)) {
				if(classNum==3) {					
					classificationPath +=libraryTitle+">"+library.getClassTitle1().trim()+">"+
							library.getClassTitle2().trim()+">"+className;
					returnValue = 3;
				}
				else if(classNum==2) {
					classificationPath +=libraryTitle+">"+library.getClassTitle1().trim()+">"+className;
					returnValue = 2;
				}
				else if(classNum==1) {
					classificationPath +=libraryTitle+">"+className;
						returnValue = 1;
					}
					partClassificationMap.get(key).setClassificationPath(classificationPath);
					return returnValue;
				}
			}
			return returnValue;
		}
	
	private static void setAttributes(List<Attributes> attributes) {
		
	    attributes.forEach(attribute->{
	    	String order = attribute.getOrderForDescription();
	    	if(order==null || order.length()<1) {
	    		attribute.setOrderForDescription("99");
	    	}
	    });
	    
		attributes.stream().sorted(Comparator.comparing(Attributes::getOrderForDescription))
		 .forEach(attribute->{
			String partType = attribute.getClassTitle().trim().toUpperCase().split(",")[0];
			List<AttributeValues> attributeValues = null;
			AttributeValues attributeValues2 = new AttributeValues();
			if(!attributeValuesMap.containsKey(partType)) {
				attributeValues2.setOrder(attribute.getOrderForDescription());
				attributeValues2.setPartTpe(attribute.getClassTitle());
				attributeValues2.setAttribute(attribute.getAttributeName());
				attributeValues2.setAuthAbrevations(attribute.getAuthorizedAbbrevation());
				attributeValues2.setAuthValues(attribute.getAuthorizedValues());
				attributeValues = new ArrayList<>();
				attributeValues.add(attributeValues2);
			}
			else {
				attributeValues	= attributeValuesMap.get(partType);
				attributeValues2.setOrder(attribute.getOrderForDescription());
				attributeValues2.setPartTpe(attribute.getClassTitle());
				attributeValues2.setAttribute(attribute.getAttributeName());
				attributeValues2.setAuthAbrevations(attribute.getAuthorizedAbbrevation());
				attributeValues2.setAuthValues(attribute.getAuthorizedValues());
				attributeValues.add(attributeValues2);
			}
			attributeValuesMap.put(partType, attributeValues);
//			System.out.println(partType+"  "+attributeValues);
		});
	}
	
	public static boolean isNumber(String number) {
		
		
		for(int i=0;i<number.length();i++) {
			char c = number.charAt(i);
			if(!(c>='0' && c<='9' || c=='.' || c=='M' || c=='m')) {
				return false;
			}
		}
		return true;
	}
	
	private static void setAttributeValues() {
		
		partClassificationMap.forEach((partNumber,partClassification)->{
			
			String[] descValues1 = partClassification.getDescription().trim().split("-");
			if(descValues1.length==0) {
				return;
			}
			String classificationPath = partClassification.getClassificationPath();
//			if(classificationPath!=null && classificationPath.length()>1) {			
//				partClassification.setPartType(descValues1[0].toUpperCase());
//			}
			
			String[] descValues = new String[descValues1.length-1];
			for(int i =1;i<descValues1.length;i++) {
				descValues[i-1] = descValues1[i].toUpperCase();
			}
			
			String partType = descValues1[0].trim().toUpperCase();
			if(partType==null) {
				partClassification.setPartType("");
				return;
			}
			else {
				partClassification.setPartType(partType);
			}
			List<AttributeValues> attributeValues = attributeValuesMap.get(partType);
			List<AttributeValues> attributeValuesList = new ArrayList<AttributeValues>();
			List<AttributeValues> attributeValuesList1 = new ArrayList<AttributeValues>();
			List<AttributeValues> attributeValuesList2 = new ArrayList<AttributeValues>();
			
			if(partType.equalsIgnoreCase("NUT") && descValues.length>0) {
				Map<Integer, AttributeValues> attributeValMap = new TreeMap<Integer, AttributeValues>();
				
				for(int i=0;i<descValues.length;i++) {
					String attributeVal = descValues[i].trim();
					AttributeValues attributeValue = new AttributeValues();
					if(attributeVal.contains("HEX") || attributeVal.contains("hex")) {
						attributeValue.setAttribute("Part Type");
						attributeValue.setAttributeValue(attributeVal);
						attributeValue.setDescriptionValue("");
						attributeValMap.put(0, attributeValue);
//						attributeValuesList.add(0,attributeValue);
					}
					else if(attributeVal.startsWith("GR") || attributeVal.equalsIgnoreCase("C")) {
						attributeValue.setAttribute("Bolt Grade / Class");
						attributeValue.setAttributeValue("");
						attributeValue.setDescriptionValue(attributeVal);
						attributeValMap.put(1, attributeValue);
//						attributeValuesList.add(1,attributeValue);
						
					}
					else if(isNumber(attributeVal) && descValues.length>i+1
							&& isNumber( descValues[i+1].trim().split(" ").length>1 ? descValues[i+1].trim().split(" ")[0] : descValues[i+1])) {
						i++;
						attributeValue.setAttribute("Nut Thread Size");
						attributeValue.setAttributeValue("");
						attributeValue.setDescriptionValue("0"+attributeVal+"-"+ (descValues[i].split(" ").length>1 ? descValues[i].split(" ")[0] : descValues[i]));
						attributeValMap.put(2, attributeValue);
//						attributeValuesList.add(1,attributeValue);
//						if(descValues.length>i+1 && descValues[i+1].split(" ").length>1) {
//							AttributeValues attributeValue1 = new AttributeValues();
//							attributeValue1.setAttribute("");
//							attributeValue1.setAttributeValue("");
//							attributeValue1.setDescriptionValue(descValues[i+1].split(" ")[1]);
//							attributeValuesList.add(attributeValue1);
//						}
					}
					else if(attributeVal.equalsIgnoreCase("PLN")) {
						attributeValue.setAttribute("Bolt Coating");
						attributeValue.setAttributeValue("");
						attributeValue.setDescriptionValue(attributeVal);
						attributeValMap.put(3, attributeValue);
//						attributeValuesList.add(3,attributeValue);
					}
					else {
						attributeValue.setAttribute("");
						attributeValue.setAttributeValue("");
						attributeValue.setDescriptionValue(attributeVal);
						attributeValuesList.add(attributeValue);
					}
				}
					
				if(attributeValMap.containsKey(0)) {
					attributeValuesList.add(0,attributeValMap.get(0));
				}
				else {
					AttributeValues attributeValue = new AttributeValues();
					attributeValue.setAttribute("Part Type");
					attributeValue.setAttributeValue("");
					attributeValue.setDescriptionValue("");
					attributeValuesList.add(0,attributeValue);
				}
				
				if(attributeValMap.containsKey(1)) {
					attributeValuesList.add(1,attributeValMap.get(1));
				}
				else {
					AttributeValues attributeValue = new AttributeValues();
					attributeValue.setAttribute("Bolt Grade / Class");
					attributeValue.setAttributeValue("");
					attributeValue.setDescriptionValue("");
					attributeValuesList.add(1,attributeValue);
				}
				
				if(attributeValMap.containsKey(2)) {
					attributeValuesList.add(2,attributeValMap.get(2));
				}
				else {
					AttributeValues attributeValue = new AttributeValues();
					attributeValue.setAttribute("Nut Thread Size");
					attributeValue.setAttributeValue("");
					attributeValue.setDescriptionValue("");
					attributeValuesList.add(2,attributeValue);
				}
				
				if(attributeValMap.containsKey(3)) {
					attributeValuesList.add(3,attributeValMap.get(3));
				}
				else {
					AttributeValues attributeValue = new AttributeValues();
					attributeValue.setAttribute("Bolt Coating");
					attributeValue.setAttributeValue("");
					attributeValue.setDescriptionValue("");
					attributeValuesList.add(3,attributeValue);
				}
//				int i=0;
//				for(Entry<Integer, AttributeValues> entry : attributeValMap.entrySet()) {
//					
//					if(entry.getKey()==0) {
//						attributeValuesList.add(0,entry.getValue());
//					}
//					else if(entry.getKey()==1) {
//						attributeValuesList.add(1,entry.getValue());
//					}
//					else if(entry.getKey()==2) {
//						attributeValuesList.add(2,entry.getValue());
//					}
//					else if(entry.getKey()==3) {
//						attributeValuesList.add(3,entry.getValue());
//					}
//					else {
//						AttributeValues attributeValue = new AttributeValues();
//						if(i==0) {
//							attributeValue.setAttribute("Part Type");
//							attributeValue.setAttributeValue("");
//							attributeValue.setDescriptionValue("");
//							attributeValuesList.add(0,attributeValue);
//						}
//						else if(i==1) {
//							attributeValue.setAttribute("Bolt Grade / Class");
//							attributeValue.setAttributeValue("");
//							attributeValue.setDescriptionValue("");
//							attributeValuesList.add(1,attributeValue);
//						}
//						else if(i==2) {
//							attributeValue.setAttribute("Nut Thread Size");
//							attributeValue.setAttributeValue("");
//							attributeValue.setDescriptionValue("");
//							attributeValuesList.add(2,attributeValue);
//						}
//						else if(i==3) {
//							attributeValue.setAttribute("Bolt Coating");
//							attributeValue.setAttributeValue("");
//							attributeValue.setDescriptionValue("");
//							attributeValuesList.add(3,attributeValue);
//						}
//						i++;
//					}
//				}
//				attributeValuesList.addAll(tempAttValues);
//				AttributeValues attributeValue1 = new AttributeValues();
//				attributeValue1.setAttribute("Part Type");
//				if(descValues[0].contains("HEX") || descValues[0].contains("hex")) {
//					attributeValue1.setAttributeValue(descValues[0]);
//					attributeValue1.setDescriptionValue("");
//				}
//				else {
//					attributeValue1.setAttributeValue("");
//					attributeValue1.setDescriptionValue(descValues[0]);
//				}
//				int index=0;
//				if(descValues.length>1 && descValues[1].equals("FLGWHIZ")) {
//					index=1;
//				}
//				AttributeValues attributeValue2 = new AttributeValues();
//				attributeValue2.setAttribute("Bolt Grade / Class");
//				attributeValue2.setAttributeValue("");
//				
//				if(descValues.length>3+index) {
//					String grade = descValues[3+index].trim();
//					if(grade.startsWith("GR") || grade.startsWith("C"))
//					     attributeValue2.setDescriptionValue(descValues[3+index]);
//					else attributeValue2.setDescriptionValue("");
//				}
//				else attributeValue2.setDescriptionValue("");
//				
//				AttributeValues attributeValue3 = new AttributeValues();
//				attributeValue3.setAttribute("Nut Thread Size");
//				attributeValue3.setAttributeValue("");
//				if(descValues.length>2+index 
//						&& isNumber(descValues[1+index]) 
//						&& isNumber( descValues[2+index].split(" ").length>1 ? descValues[2+index].split(" ")[0] : descValues[2+index])) {
//					attributeValue3.setDescriptionValue(descValues[1+index]+"  -  "+ (descValues[2+index].split(" ").length>1 ? descValues[2+index].split(" ")[0] : descValues[2+index]));
//				}
//				else attributeValue3.setDescriptionValue("");
//				
//				AttributeValues attributeValue4 = new AttributeValues();
//				attributeValue4.setAttribute("Bolt Coating");
//				attributeValue4.setAttributeValue("");
//				if(descValues.length>4 && index!=0) {
//					String boltCoating = descValues[descValues.length-1];
//					if(boltCoating.equalsIgnoreCase("PLN"))
//					     attributeValue4.setDescriptionValue(boltCoating);
//					else attributeValue4.setDescriptionValue("");
//				}
//				else attributeValue4.setDescriptionValue("");
////				else attributeValue2.setDescriptionValue(boltCoating);
//				
//				attributeValuesList.add(attributeValue1);
//				attributeValuesList.add(attributeValue2);
//				attributeValuesList.add(attributeValue3);
//				attributeValuesList.add(attributeValue4);
//				
//				if(descValues.length>3) {
//					for(int i=4;i<descValues.length-1;i++) {
//						AttributeValues attributeValue = new AttributeValues();
//						attributeValue.setAttribute("");
//						attributeValue.setAttributeValue("");
//						attributeValue.setDescriptionValue(descValues[i]);
//						attributeValuesList.add(attributeValue);
//					}
//				}
			}
			
			else if(partType.equalsIgnoreCase("SCREW") && descValues.length>0) {
				AttributeValues attributeValue1 = new AttributeValues();
				attributeValue1.setAttribute("Screw Type");
				attributeValue1.setAttributeValue("");
				attributeValue1.setDescriptionValue(descValues[0]);
				
				AttributeValues attributeValue2 = new AttributeValues();
				attributeValue2.setAttribute("Screw Thread Size");
				attributeValue2.setAttributeValue("");
				if(descValues.length>1)
				attributeValue2.setDescriptionValue(descValues[1]);
				else attributeValue2.setDescriptionValue("");
				
				AttributeValues attributeValue3 = new AttributeValues();
				attributeValue3.setAttribute("Length");
				attributeValue3.setAttributeValue("");
				String[] lengths = new String[2];
				if(descValues.length>2) {					
					lengths = descValues[2].split("X");
				}
				if(lengths.length>2)
				attributeValue3.setDescriptionValue(lengths[1]);
				else attributeValue3.setDescriptionValue("");
				
				AttributeValues attributeValue4 = new AttributeValues();
				attributeValue4.setAttribute("Bolt Grade/Class");
				attributeValue4.setAttributeValue("");
				if(descValues.length>3 && descValues[3].contains("G"))
				 attributeValue4.setDescriptionValue(descValues[3]);
				else {
					attributeValue4.setDescriptionValue("");
					if(descValues.length>3)
					attributeValue3.setDescriptionValue(descValues[2]+descValues[3]);
					else attributeValue3.setDescriptionValue("");
				}
				AttributeValues attributeValue5 = new AttributeValues();
				attributeValue5.setAttribute("Screw Finish");
				attributeValue5.setAttributeValue("");
				attributeValue5.setDescriptionValue("");
				
				attributeValuesList.add(attributeValue1);
				attributeValuesList.add(attributeValue2);
				attributeValuesList.add(attributeValue3);
				attributeValuesList.add(attributeValue4);
				attributeValuesList.add(attributeValue5);
				AttributeValues attributeValue = new AttributeValues();
				attributeValue.setAttribute("");
				attributeValue.setAttributeValue("");
				attributeValue.setDescriptionValue(lengths[0]);
				attributeValuesList.add(attributeValue);
				if(descValues.length==5) {
					AttributeValues attributeValue6 = new AttributeValues();
					attributeValue6.setAttribute("");
					attributeValue6.setAttributeValue("");
					attributeValue6.setDescriptionValue(descValues[4]);
					attributeValuesList.add(attributeValue6);
				}
			}
			else if(partType.equalsIgnoreCase("WASHER") && descValues.length>0) {

				AttributeValues attributeValue1 = new AttributeValues();
				attributeValue1.setAttribute("Washer Type");
				attributeValue1.setAttributeValue("");
				attributeValue1.setDescriptionValue(descValues[0]);
				
				AttributeValues attributeValue2 = new AttributeValues();
				attributeValue2.setAttribute("Washer Material");
				attributeValue2.setAttributeValue("");
				attributeValue2.setDescriptionValue("");
				
				AttributeValues attributeValue3 = new AttributeValues();
				attributeValue3.setAttribute("Washer Grade/Class");
				attributeValue3.setAttributeValue("");
				attributeValue3.setDescriptionValue("");
				
				AttributeValues attributeValue4 = new AttributeValues();
				attributeValue4.setAttribute("");
				attributeValue4.setAttributeValue("Washer Coating");
				attributeValue4.setDescriptionValue("");
				
				AttributeValues attributeValue5 = new AttributeValues();
				attributeValue5.setAttribute("Washer ID");
				attributeValue5.setAttributeValue("");
				if(descValues.length>1)
				attributeValue5.setDescriptionValue(descValues[1]);
				else attributeValue5.setDescriptionValue("");
				
				AttributeValues attributeValue6 = new AttributeValues();
				attributeValue6.setAttribute("Washer OD");
				attributeValue6.setAttributeValue("");
				if(descValues.length>2)
				   attributeValue6.setDescriptionValue(descValues[2].split("X")[0]);
				else attributeValue6.setDescriptionValue("");
				
				AttributeValues attributeValue7 = new AttributeValues();
				attributeValue7.setAttribute("Washer Thickness");
				attributeValue7.setAttributeValue("");
				if(descValues.length>3)
				attributeValue7.setDescriptionValue(descValues[3]);
				else attributeValue7.setDescriptionValue("");
				
				
				attributeValuesList.add(attributeValue1);
				attributeValuesList.add(attributeValue2);
				attributeValuesList.add(attributeValue3);
				attributeValuesList.add(attributeValue4);
				attributeValuesList.add(attributeValue5);
				attributeValuesList.add(attributeValue6);
				attributeValuesList.add(attributeValue7);
			}
			else if(attributeValues!=null) {
				Set<String> attributeNames = new HashSet<String>();
				for(int i=0;i<descValues.length;i++) {
					boolean isPresent = false;
					for(int j=0;j<attributeValues.size();j++) {
						AttributeValues attributeValue = attributeValues.get(j);
						String[] attributeAbrevations = attributeValue.getAuthAbrevations().trim().split(",");
						String[] attributeAbrevationValues = attributeValue.getAuthValues().trim().split(",");
						Set<String> attAbrevationSet = new HashSet<String>();
						for(int k=0;k<attributeAbrevations.length;k++) {
							attAbrevationSet.add(attributeAbrevations[k].trim().toUpperCase());
						}
						AttributeValues attributeValue1 = new AttributeValues();
					    if(attAbrevationSet.contains(descValues[i]) && j<attributeAbrevationValues.length) {
							
								attributeValue1.setAttribute(attributeValue.getAttribute());
								String partType1 = attributeValue.getPartTpe();
								if(partType1==null) attributeValue1.setPartTpe("");
								attributeValue1.setAttributeValue(attributeAbrevationValues[j]);
								attributeValue1.setDescriptionValue("");
								attributeValuesList.add(attributeValue1);
								isPresent = true;
						}
						else if(!attributeNames.contains(attributeValue.getAttribute())) {
							attributeNames.add(attributeValue.getAttribute());
							attributeValue1.setAttribute(attributeValue.getAttribute());
							attributeValue1.setAttributeValue("");
							attributeValue1.setDescriptionValue("");
							attributeValuesList1.add(attributeValue1);
						}
					}
					
					if(!isPresent) {
						AttributeValues attributeValue2 = new AttributeValues();
						attributeValue2.setAttribute("");
						attributeValue2.setAttributeValue("");
						attributeValue2.setDescriptionValue(descValues[i]);
						attributeValuesList2.add(attributeValue2);
					}
				}
				attributeValuesList.addAll(attributeValuesList1);
				attributeValuesList.addAll(attributeValuesList2);
				
			}
			else {
				for(int i=0;i<descValues.length;i++) {
					AttributeValues attributeValue1 = new AttributeValues();
					attributeValue1.setAttribute("");
					attributeValue1.setAttributeValue("");
//					attributeValue1.setPartTpe(descValues1[0]);
					attributeValue1.setDescriptionValue(descValues[i]);
				    attributeValuesList.add(attributeValue1);
				}
			}
			partClassification.setAttributeValues(attributeValuesList);
		});
		
	}

	private static List<PartClassification> getPartClassifications(){
	    	List<PartClassification> partClassifications = new ArrayList<PartClassification>();
	    	partClassificationMap.forEach((key,value)->{
	    		partClassifications.add(value);
	    	});
	    	return partClassifications;
	}
}






