package com.veremeer_library.veremeer_library_project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.SupportedSourceVersion;

import com.veremeer_library.veremeer_library_project.model.AttributeValues;
import com.veremeer_library.veremeer_library_project.model.Attributes;
import com.veremeer_library.veremeer_library_project.model.PartClassification;
import com.veremeer_library.veremeer_library_project.model.RawData;
import com.veremeer_library.veremeer_library_project.model.VermeerLibrary;

public class ProcessData {
	
	private static Map<String, PartClassification> partClassificationMap = new LinkedHashMap<String, PartClassification>();
	private static Map<String, List<AttributeValues>> attributeValuesMap = new LinkedHashMap<>();
	
	public static List<PartClassification> processPartClassificationData(List<VermeerLibrary> libraryData, 
		List<RawData> rawData, List<Attributes> attributes){
		
		processPartClassification(rawData);
		setTypeAndClassificationData(libraryData,attributes);
		setPartTypeAndAttributes(attributes);
		setPartTypeInPartClassification();
		setAttributeValues();
		
//		setAttributes(attributes);
//		setAttributeValues1(attributes);
		
		return getPartClassifications();
	}
	
	private static void processPartClassification(List<RawData> rawData) {
		
		rawData.forEach(data->{
			PartClassification partClassification = new PartClassification();
			partClassification.setPartNumber(data.getPartNumber());
			partClassification.setDescription(data.getDecription());
			String type = data.getDecription().split("-")[0].toLowerCase();
			partClassification.setPartType(type);
			partClassificationMap.put(data.getPartNumber(), partClassification);
		});
	}
	
	public static void setPartTypeAndAttributes(List<Attributes> attributes) {
		
		attributes.stream().forEach(data->{
			if(data.getOrderForDescription().length()==0) {
				data.setOrderForDescription("99");
			}
		});
		attributes.stream()
		    .sorted(Comparator.comparing(Attributes::getOrderForDescription))
		    .forEach((attribute)->{
		    	String attType = attribute.getClassTitle().trim();
		    	String order = attribute.getOrderForDescription();
		    	if(attType!=null && attType.length()>1 ) {
		    		if(!attributeValuesMap.containsKey(attType)) {
						AttributeValues attributeValue = new AttributeValues();
						attributeValue.setAttribute(attribute.getAttributeName());
						attributeValue.setAuthAbrevations(attribute.getAuthorizedAbbrevation());
						attributeValue.setAuthValues(attribute.getAuthorizedValues());
						attributeValue.setOrder(order);
			
						List<AttributeValues> attributeValues = new ArrayList<AttributeValues>();
						attributeValues.add(attributeValue);
						attributeValuesMap.put(attType, attributeValues);
					}
					else if(attributeValuesMap.containsKey(attType)){
						List<AttributeValues> attributeValues = attributeValuesMap.get(attType);
						AttributeValues attributeValue = new AttributeValues();
						attributeValue.setAttribute(attribute.getAttributeName());
						attributeValue.setAuthAbrevations(attribute.getAuthorizedAbbrevation());
						attributeValue.setAuthValues(attribute.getAuthorizedValues());
						attributeValue.setOrder(order);
						attributeValues.add(attributeValue);
					}
		    	}
				
		    });
	   }
	
	 private static void setPartTypeInPartClassification() {
			
			partClassificationMap.forEach((partType,partClassification)->{
			       String type = partClassification.getPartType().trim().toLowerCase();
			       if(attributeValuesMap.containsKey(type)) {
			    	   partClassification.setAttributeValues(attributeValuesMap.get(type));
			       }
			});
	}
	 
	 static int count =0;
	 private static void setAttributeValues() {
		 
		 partClassificationMap.forEach((key,partClassification)->{
			 count++;
			 
			 List<AttributeValues> attbValuesList = null;
			 String[] attDescValues = partClassification.getDescription().trim().split("-");
			 String partType = partClassification.getPartType().trim();
			 partType = partType.substring(0,1).toUpperCase()+partType.substring(1,partType.length());
			 
//			 System.out.println("Part Type "+partType+" ");
			 if(attributeValuesMap.containsKey(partType)){   
				 List<AttributeValues> attValues = attributeValuesMap.get(partType);   
				 List<AttributeValues> attributeValuesList = new ArrayList<AttributeValues>(20);
				 partClassification.setPartType(partType.toUpperCase());
				 for(AttributeValues attributeValue : attValues) {
					 String[] attAbreavations = attributeValue.getAuthAbrevations().trim().split(",");
					 String[] attAbrevValues = attributeValue.getAuthValues().trim().split(",");
					 
					 Map<String, Integer> attAbvMap = new HashMap<String, Integer>();
					 for(int i=0;i<attAbreavations.length;i++) {
						 attAbvMap.put(attAbreavations[i].trim().toLowerCase(), i);
					 }
					 attbValuesList = new ArrayList<AttributeValues>();
					 List<AttributeValues> attributeValList1 = new ArrayList<AttributeValues>();
					 for(int j=1;j<attDescValues.length;j++) {
						 AttributeValues attrVal = new AttributeValues();
						 if(attAbvMap.containsKey(attDescValues[j].trim().toLowerCase())) {
							attrVal.setAttribute(attributeValue.getAttribute());
							String abrVal = attAbrevValues[attAbvMap.get(attDescValues[j].trim().toLowerCase())];
							attrVal.setAttributeValue(abrVal);
							attrVal.setDescriptionValue("");
							attributeValuesList.add(attrVal);
						 }
						 else {
							 attrVal.setAttribute(attributeValue.getAttribute());
							 attrVal.setAttributeValue("");
							 attrVal.setDescriptionValue("");
							 attributeValuesList.add(attrVal);
							 AttributeValues attrVal1 = new AttributeValues();
							 attrVal1.setDescriptionValue(attDescValues[j]);
							 attrVal1.setAttribute("");
							 attrVal1.setAttributeValue("");
							 attributeValList1.add(attrVal1);
						 }
				     }
					 for(AttributeValues attributeValues : attributeValList1)attributeValuesList.add(attributeValue);
//					 attributeValuesList.addAll(attributeValList1);
					 partClassification.setAttributeValues(attributeValuesList);
			    } 
		    }
		});	
	 }
	
    private static List<PartClassification> getPartClassifications(){
    	List<PartClassification> partClassifications = new ArrayList<PartClassification>();
    	partClassificationMap.forEach((key,value)->{
    		partClassifications.add(value);
    	});
    	return partClassifications;
    }
    
    
    
    
    
    
    
    
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////    
	public static void setTypeAndClassificationData(List<VermeerLibrary> libraryList, List<Attributes> attributes) {
		
		partClassificationMap.forEach((key,val)->{
			String[] descriptions = val.getDescription().toLowerCase().trim().split("-");
			
			String type = descriptions[0].trim(); 
			libraryList.forEach(data->{
				String[] arr3 = data.getClassTitle3().toLowerCase().split(",");
				String[] arr2 = data.getClassTitle2().toLowerCase().split(",");
				String[] arr1 = data.getClassTitle3().toLowerCase().split(",");
				int returnValue = 0;
			    returnValue	= setPartClassificationData(arr3, type, key, 3, data);
			    if(returnValue==0)
			    returnValue = setPartClassificationData(arr2, type, key, 2, data);
			    if(returnValue==0)
			    returnValue = setPartClassificationData(arr1, type, key, 1, data);
			});
			String[] values = new String[descriptions.length-1];
			for(int i=0;i<values.length;i++) 
				            values[i] = descriptions[i+1];
			
//			setAttributeValues(values,attributes,val);
		});
	}
	
	private static int setPartClassificationData(String[] classes, String type, String key,int classNum,VermeerLibrary library) {
		
		String libraryTitle = library.getLibraryTitle().trim();
		String classificationPath = "";
		int returnValue = 0;
		for(String className : classes) {
			if(className.toLowerCase().equals(type)) {
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
				partClassificationMap.get(key).setPartType(className);
				partClassificationMap.get(key).setClassificationPath(classificationPath);
				return returnValue;
			}
		}
		return returnValue;
	}
	/*
	public static void setAttributeValues1(List<Attributes> attributes) {
		
		  partClassificationMap.forEach((key,partClassification)->{
			String[] descriptions = partClassification.getDescription().toLowerCase().trim().split("-");
			String[] values = new String[descriptions.length-1];
			for(int i=0;i<values.length;i++)  values[i] = descriptions[i+1];
			
		    List<Attributes> filteredAttributes =	attributes.stream().filter(attribute->{
				        	String attrributeValue = attribute.getClassTitle();
				        	String partType = partClassification.getPartType();
				        	if(attrributeValue!=null && partType!=null) {
				        		return attrributeValue.toLowerCase().trim().equals(partType.toLowerCase().trim());
				        	}
						       return false;
					           }).sorted(Comparator.comparing(Attributes::getOrderForDescription)).collect(Collectors.toList());
//		  List<AttributeValues> attributeValues = new ArrayList<AttributeValues>();
		  
	        filteredAttributes.forEach(attribute->{
	        	
	        	String[] authorizedAbbrevation = attribute.getAuthorizedAbbrevation().trim().toLowerCase().split(",");
	        	String[] authorizedValues = attribute.getAuthorizedValues().trim().toLowerCase().split(",");
	        	String descOrder = attribute.getOrderForDescription().trim();
	        	
	        	int order = -1;
	        	if(descOrder!=null && descOrder.length()!=0 && checkIsNumber(descOrder)) {
//	        		System.out.println(descOrder);
	        		order = Integer.parseInt(descOrder);
	        	}
	        	
	        	for(int j=0;j<values.length;j++) {
	        		
	        		for(int i=0;i<authorizedAbbrevation.length;i++) {
	        			
	        			String authAbbrevation = authorizedAbbrevation[i].trim().toLowerCase();
	        			String[] authAbbrValues = authAbbrevation.split("-");
	        			
	        			if(order!=-1 && authAbbrValues.length==2){
	        				if(authAbbrValues[0].startsWith("m")) {
	        					authAbbrValues[0]=authAbbrValues[0].substring(1,authAbbrValues[0].length());
	        				}
	        				if(values[j].trim().startsWith("m")) {
	        					values[j] = values[j].trim().substring(1,values[j].trim().length());
	        				}
	        				if(checkIsNumber(values[j].trim()) && checkIsNumber(authAbbrValues[0])&& checkIsNumber(authAbbrValues[1])) {
	        					float val1 = Float.parseFloat(authAbbrValues[0]);
	        					float val2 = Float.parseFloat(authAbbrValues[1]);
	        					float descrValue = Float.parseFloat(values[j].trim());
	        					if(descrValue<=val2 || descrValue>=val1) {
//	        						partClassification.setValue2(authorizedAbbrevation[i]);
//	        						setPartClassifivationValues(partClassification,authorizedValues,values,authAbbrevation,descOrder,i,j);
	        						if(partClassification.getValue2()==null && partClassification.getPartType().trim().toLowerCase().equals("screw")) {
	        							partClassification.setValue2(authorizedAbbrevation[i]);
	        						}
	        						else if(partClassification.getValue3()==null && partClassification.getPartType().trim().toLowerCase().equals("nut")) {
	        							partClassification.setValue3(authorizedAbbrevation[i]);
	        				    }
	        				}
	        			} 
	        			else  {
	        				String authAbb = authorizedAbbrevation[i].trim();
		        			if(order!=-1 && values[j].trim().equalsIgnoreCase(authAbb) ) {
		        				if(order==1) {
		        					partClassification.setValue1(authorizedValues[i]);
		        				}
		        				else if(order==2) {
		        					partClassification.setValue2(authorizedValues[i]);
		        				}
		        				else if(order==3) {
		        					partClassification.setValue3(authorizedValues[i]);
		        				}
		        				else if(order==4) {
		        					partClassification.setValue4(authorizedValues[i]);
		        				}
		        				else if(order==5) {
		        					partClassification.setValue5(authorizedValues[i]);
		        				}
		        				else if(order==6) {
		        					partClassification.setValue6(authorizedValues[i]);
		        				}
		        			}
		        			else {
		        				if(partClassification.getDummyValue1()==null && partClassification.getAttribute1()==null || 
		        						partClassification.getDummyValue1()=="" && partClassification.getAttribute1()=="") {
		        					partClassification.setDummyValue1(values[j]);
		        					System.out.println(partClassification.getAttribute1());
		        				}
		        				else if(partClassification.getDummyValue2()==null && partClassification.getAttribute2()==null ||
		        						partClassification.getDummyValue2()=="" && partClassification.getAttribute2()=="") {
		        					partClassification.setDummyValue2(values[j]);
		        				}
		        				else if(partClassification.getDummyValue3()==null && partClassification.getAttribute3()==null
		        						|| partClassification.getDummyValue3()=="" && partClassification.getAttribute3()=="") {
		        					partClassification.setDummyValue3(values[j]);
		        				}
		        		    	else if(partClassification.getDummyValue4()==null && partClassification.getAttribute4()==null
		        		    			|| partClassification.getDummyValue4()=="" && partClassification.getAttribute4()=="") {
		        					partClassification.setDummyValue4(values[j]);
		        				}
		        				else if(partClassification.getDummyValue5()==null && partClassification.getAttribute5()==null
		        						|| partClassification.getDummyValue5()=="" && partClassification.getAttribute5()=="") {
		        					partClassification.setDummyValue5(values[j]);
		        				}
		        				else if(partClassification.getDummyValue6()==null && partClassification.getAttribute6()==null
		        						|| partClassification.getDummyValue6()=="" && partClassification.getAttribute6()=="") {
		        					partClassification.setDummyValue6(values[j]);
		        				}
		        				else if(partClassification.getDummyValue7()==null && partClassification.getAttribute7()==null
		        						|| partClassification.getDummyValue7()=="" && partClassification.getAttribute7()=="") {
		        					partClassification.setDummyValue7(values[j]);
		        				}
		        		    	else if(partClassification.getDummyValue8()==null && partClassification.getAttribute8()==null
		        		    			|| partClassification.getDummyValue8()=="" && partClassification.getAttribute8()=="") {
		        					partClassification.setDummyValue8(values[j]);
		        				}
		        				else if(partClassification.getDummyValue9()==null && partClassification.getAttribute9()==null
		        						|| partClassification.getDummyValue9()=="" && partClassification.getAttribute9()=="") {
		        					partClassification.setDummyValue9(values[j]);
		        				}
		        				else if(partClassification.getDummyValue10()==null && partClassification.getAttribute10()==null
		        						|| partClassification.getDummyValue10()=="" && partClassification.getAttribute10()=="") {
		        					partClassification.setDummyValue10(values[j]);
		        				}
		        			}
		        		}
	        		}
	        	  }
	        	}
		  });
	   }); 
	}
	private static Boolean checkIsNumber(String val) {
		for(char c : val.toCharArray()) {
			if(!((c>='0' && c<='9') || c=='.')) {
				return false;
			}
		}
		return true;
	}
	public static void setAttributeValues(String[] values, 
			List<Attributes> attributes,
			PartClassification partClassification) {
		
	        List<Attributes> filteredAttributes =	attributes.stream().filter(attribute->{
	        	String attrributeValue = attribute.getClassTitle();
	        	String partType = partClassification.getPartType();
	        	if(attrributeValue!=null && partType!=null) {
	        		return attrributeValue.toLowerCase().trim().equals(partType.toLowerCase().trim());
	        	}
			       return false;
		           }).sorted(Comparator.comparing(Attributes::getOrderForDescription)).collect(Collectors.toList());
	        
	        filteredAttributes.forEach(attribute->{
	        	String[] authorizedAbbrevation = attribute.getAuthorizedAbbrevation().trim().toLowerCase().split(",");
	        	String[] authorizedValues = attribute.getAuthorizedValues().trim().toLowerCase().split(",");
	        	String descOrder = attribute.getOrderForDescription();
	        	
	        	for(int j=0;j<values.length;j++) {
	        		for(int i=0;i<authorizedAbbrevation.length;i++) {
	        			String authAbbrevation = authorizedAbbrevation[i].trim().toLowerCase();
	        			String[] authAbbrValues = authAbbrevation.split("-");
	        			
	        			if(authAbbrValues.length==2){
	        				if(authAbbrValues[0].startsWith("m")) {
	        					authAbbrValues[0]=authAbbrValues[0].substring(1,authAbbrValues[0].length());
	        				}
	        				if(values[j].trim().startsWith("m")) {
	        					values[j] = values[j].trim().substring(1,values[j].trim().length());
	        				}
	        				if(checkIsNumber(values[j].trim()) && checkIsNumber(authAbbrValues[0])&& checkIsNumber(authAbbrValues[1])) {
	        					float val1 = Float.parseFloat(authAbbrValues[0]);
	        					float val2 = Float.parseFloat(authAbbrValues[1]);
	        					float descrValue = Float.parseFloat(values[j].trim());
	        					if(descrValue<=val2 || descrValue>=val1) {
//	        						partClassification.setValue2(authorizedAbbrevation[i]);
//	        						setPartClassifivationValues(partClassification,authorizedValues,values,authAbbrevation,descOrder,i,j);
	        						if(partClassification.getValue2()==null && partClassification.getPartType().trim().toLowerCase().equals("screw")) {
	        							partClassification.setValue2(authorizedAbbrevation[i]);
	        						}
	        						else if(partClassification.getValue3()==null && partClassification.getPartType().trim().toLowerCase().equals("nut")) {
	        							partClassification.setValue3(authorizedAbbrevation[i]);
	        				}
	        					}
	        					else {
	        						if(partClassification.getValue2()==null && partClassification.getPartType().trim().toLowerCase().equals("screw")) {
	        							partClassification.setDummyValue2(authorizedAbbrevation[i]);
	        						}
	        						else if(partClassification.getValue3()==null && partClassification.getPartType().trim().toLowerCase().equals("nut") ) {
	        							partClassification.setDummyValue3(authorizedAbbrevation[i]);
	        						}
	        					}
	        					
	        				}
	        			}
//	        			else if(true)setPartClassifivationValues(partClassification,authorizedValues,values,authAbbrevation,descOrder,i,j);
	        			else if((partClassification.getValue1()==null && 
	        						partClassification.getValue2()==null && partClassification.getValue3()==null
	        						&&partClassification.getValue4()==null &&partClassification.getValue5()==null
	        						&&partClassification.getValue6()==null) && authorizedValues.length>i && descOrder != null)                                  
	        				{
		        				if(values[j].trim().equals(authAbbrevation)) {
		        					if(descOrder.equals("1"))partClassification.setValue1(authorizedValues[i]);
		        					else if(descOrder.equals("2"))partClassification.setValue2(authorizedValues[i]);
		        					else if(descOrder.equals("3"))partClassification.setValue3(authorizedValues[i]);
		        					else if(descOrder.equals("4"))partClassification.setValue4(authorizedValues[i]);
		        					else if(descOrder.equals("5"))partClassification.setValue5(authorizedValues[i]);
		        					else if(descOrder.equals("6"))partClassification.setValue6(authorizedValues[i]);
		        				}
		        				else if(!descOrder.equals("1")&& !descOrder.equals("2") && 
		        						!descOrder.equals("3") && !descOrder.equals("4")&& 
		        						!descOrder.equals("5") && !descOrder.equals("6") &&
		        						partClassification.getValue1()==null && 
		        						partClassification.getValue2()==null && partClassification.getValue3()==null
		        						&&partClassification.getValue4()==null &&partClassification.getValue5()==null
		        						&&partClassification.getValue6()==null) {
		        					
			        					if(descOrder.equals("1"))partClassification.setDummyValue1(authorizedValues[i]);
			        					else if(descOrder.equals("2"))partClassification.setDummyValue2(authorizedValues[i]);
			        					else if(descOrder.equals("3"))partClassification.setDummyValue3(authorizedValues[i]);
			        					else if(descOrder.equals("4"))partClassification.setDummyValue4(authorizedValues[i]);
			        					else if(descOrder.equals("5"))partClassification.setDummyValue5(authorizedValues[i]);
			        					else if(descOrder.equals("6"))partClassification.setDummyValue6(authorizedValues[i]);
		        				}
	        			else  {
	        				
	        				if(partClassification.getAttribute1()==null ) {
	        					partClassification.setDummyValue1(values[j]);
	        				}
	        				else if(partClassification.getAttribute2()==null ) {
	        					partClassification.setDummyValue2(values[j]);
	        				}
	        				else if(partClassification.getAttribute3()==null ) {
	        					partClassification.setDummyValue3(values[j]);
	        				}
	        				else if(partClassification.getAttribute4()==null ) {
	        					partClassification.setDummyValue5(values[j]);
	        				}
	        				else if(partClassification.getAttribute5()==null ) {
	        					partClassification.setDummyValue5(values[j]);
	        				}
	        				else if(partClassification.getAttribute6()==null ) {
	        					partClassification.setDummyValue6(values[j]);
	        				}
	        			}
		                
	        			}
	        		  }  
	        	   }
	        });
	}
	public static void setPartClassifivationValues(PartClassification partClassification, String[] authorizedValues, String[] values,String authAbbrevation,String descOrder,int i,int j  ) {
		if((partClassification.getValue1()==null && 
				partClassification.getValue2()==null && partClassification.getValue3()==null
				&&partClassification.getValue4()==null &&partClassification.getValue5()==null
				&&partClassification.getValue6()==null) && authorizedValues.length>i && descOrder != null)                                  
		{
			if(values[j].trim().contains(authAbbrevation)) {
				if(descOrder.equals("1"))partClassification.setValue1(authorizedValues[i]);
				else if(descOrder.equals("2"))partClassification.setValue2(authorizedValues[i]);
				else if(descOrder.equals("3"))partClassification.setValue3(authorizedValues[i]);
				else if(descOrder.equals("4"))partClassification.setValue4(authorizedValues[i]);
				else if(descOrder.equals("5"))partClassification.setValue5(authorizedValues[i]);
				else if(descOrder.equals("6"))partClassification.setValue6(authorizedValues[i]);
			}
			else if(partClassification.getValue1()==null && 
					partClassification.getValue2()==null && partClassification.getValue3()==null
					&&partClassification.getValue4()==null &&partClassification.getValue5()==null
					&&partClassification.getValue6()==null) {
				
    					if(descOrder.equals("1"))partClassification.setDummyValue1(authorizedValues[i]);
    					else if(descOrder.equals("2"))partClassification.setDummyValue2(authorizedValues[i]);
    					else if(descOrder.equals("3"))partClassification.setDummyValue3(authorizedValues[i]);
    					else if(descOrder.equals("4"))partClassification.setDummyValue4(authorizedValues[i]);
    					else if(descOrder.equals("5"))partClassification.setDummyValue5(authorizedValues[i]);
    					else if(descOrder.equals("6"))partClassification.setDummyValue6(authorizedValues[i]);
			}
		}
	}
	
	
	private static void setAttributes(List<Attributes> attributes) {
		
		partClassificationMap.forEach((key,val)->{
			
			String classType = val.getPartType();
			String [] attributesAndValues = val.getDescription().toLowerCase().trim().split("-");
			
			if(classType!=null) {
				int count = 0;
				List<Attributes> filteredAttributes  = attributes.stream().filter((attribute)->
                                         attribute.getClassTitle().toLowerCase().trim().equals(classType.toLowerCase()))
			        		            .sorted(Comparator.comparing(Attributes::getOrderForDescription))
			        		            .collect(Collectors.toList());
				
				filteredAttributes.stream().forEach(attribute->{
					PartClassification partClassification = partClassificationMap.get(key);
					if(partClassification.getPartType().toLowerCase().trim()
							.equals(attribute.getClassTitle().toLowerCase().trim())) {
						String order = attribute.getOrderForDescription().trim();
						if(order.equals("1"))
						            partClassification.setAttribute1(attribute.getAttributeName());
						else if(order.equals("2"))
				            partClassification.setAttribute2(attribute.getAttributeName());
						else if(order.equals("3"))
				            partClassification.setAttribute3(attribute.getAttributeName());
						else if(order.equals("4"))
				            partClassification.setAttribute4(attribute.getAttributeName());
						else if(order.equals("5"))
				            partClassification.setAttribute5(attribute.getAttributeName());
						else {
							if(partClassification.getAttribute1()==null) {
								partClassification.setAttribute1(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute2()==null) {
								partClassification.setAttribute2(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute3()==null) {
								partClassification.setAttribute3(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute4()==null) {
								partClassification.setAttribute4(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute5()==null) {
								partClassification.setAttribute5(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute6()==null) {
								partClassification.setAttribute6(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute7()==null) {
								partClassification.setAttribute7(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute8()==null) {
								partClassification.setAttribute8(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute9()==null) {
								partClassification.setAttribute9(attribute.getAttributeName());
							}
							else if(partClassification.getAttribute10()==null) {
								partClassification.setAttribute11(attribute.getAttributeName());
							}
						}
					}
				});
			}
		});	
	}
    
	  */
}


