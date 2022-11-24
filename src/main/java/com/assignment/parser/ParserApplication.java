package com.assignment.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.opencsv.CSVReader;

@SpringBootApplication
public class ParserApplication {

	public static void main(String[] args) throws FileNotFoundException {
		
		File csvFile = new File("src/main/java/result.csv");
		
		/* this loop is taken for given data folder for json file 
		 to parse all files in loop and result.csv file gives output */
		
		for(int i=5;i<=17808;i++) {
			File jsonFile = new File("src/main/java/data/"+i+".json");
			if(jsonFile.exists()) {
				JsonToCsv(jsonFile, csvFile);
			}
		}
		
		CSVReader reader = new CSVReader(new FileReader(csvFile));

		for(String[] row : reader) {
		    System.out.println(Arrays.toString(row));
		}
		
		
		SpringApplication.run(ParserApplication.class, args);
	}
	
	public static void JsonToCsv(File jsonFile, File csvFile){
		try {
	        JsonNode jsonTree = new ObjectMapper().readTree(jsonFile);
	        Builder csvSchemaBuilder = CsvSchema.builder();
	        
	        JsonNode firstObject = jsonTree.elements().next();
	        
	        
	        
	        firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
	        	
	        CsvSchema csvSchema = csvSchemaBuilder.build()
	                .withHeader();
	        
            OutputStream outstream = new FileOutputStream(csvFile , true);
            
            CsvMapper csvMapper = new CsvMapper();

       	 	csvMapper.writerFor(JsonNode.class)
       	 		.with(csvSchema)
       	 			.writeValue(outstream, jsonTree);
           
        }catch(Exception e) {
        	OutputStream os = null;
	        try {
	        	String errorMsg = jsonFile.getAbsolutePath()+ ": \n " +e.getMessage();
	            os = new FileOutputStream(new File("src/main/java/error.txt"));
	            os.write(errorMsg.getBytes(), 0, errorMsg.length());
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }finally{
	            try {
	                os.close();
	            } catch (IOException e2) {
	                e2.printStackTrace();
	            }
	        }

        }
        
    }
	
}
	
