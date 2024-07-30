package com.veremeer_library.veremeer_library_project.process_csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class ReadCSV<T> {
	private final String relativePath ="src/resources/";
	private final String fileName;
    private final Class<T> className;
    private final String [] params;
    private List<T> classData = new ArrayList<>();
    private Map<String, Method> methodMap = new LinkedHashMap<String, Method>();
    
	public ReadCSV(String fileName,Class<T> className,String [] params) {
		this.fileName = relativePath+fileName;
		this.className = className;
		this.params = params;
		setData();
	}
	
	public List<T> readFile(boolean header) {
	
		try {
			CSVReader csvReader = new CSVReader(new FileReader(fileName));
			String [] data;
			if(header)csvReader.readNext();
			while((data=csvReader.readNext())!=null) {
				T object = (T) className.newInstance();
				classData.add(object);
				for(int i=0;i<params.length;i++) {
					Method method = methodMap.get(("set"+params[i]).toLowerCase());
					if(method!=null)method.invoke(object, data[i]);
				}
			}
			
		}catch (IllegalAccessException e) {
			
			e.printStackTrace();
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} catch (CsvValidationException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			
			e.printStackTrace();
		}
		
		return classData;
		
	}
	private void setData() {
		
	    for(String param : params) {
			methodMap.put(("set"+param).toLowerCase(), null);
		}
		try {
			Method[] methods = className.getMethods();
			for(Method m : methods) {	
				if(m.getParameterCount()>0) {
					if(methodMap.containsKey(m.getName().toLowerCase())) {
						methodMap.put(m.getName().toLowerCase(), m);					
					}
					else {
						if(m.getName().startsWith("set"))
						throw new NoSuchMethodException("method with name "+m.getName()+ " not found");
					} 
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} 
	}

}





