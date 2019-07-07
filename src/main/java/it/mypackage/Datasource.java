package it.mypackage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Datasource {
	
	private String dsFile = "\\products.xlsx";
    private Workbook workbook = null;
    private HashMap<String, Product> productsTable = null;

    public Datasource(String path) throws IOException, IllegalArgumentException, EncryptedDocumentException, InvalidFormatException {
        //URL resource = getClass().getClassLoader().getResource("products.xlsx");
    	File file = new File(path + dsFile);
        if (file.exists()) {
            workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet("Products");     
            productsTable = new HashMap<String, Product>();
            
            sheet.shiftRows(1, sheet.getLastRowNum(), -1);            
            sheet.forEach(row -> {
            	String name = row.getCell(0).getStringCellValue().trim();
            	String type = row.getCell(1).getStringCellValue().trim();
            	Boolean isTaxFree = Boolean.valueOf(row.getCell(2).getStringCellValue().trim().toLowerCase());
            	Product product = new Product(name, type, isTaxFree);
            	productsTable.put(name, product);
            });
        } else {            
            throw new IllegalArgumentException("Datasource not found!");
        }
	}
    
    public HashMap<String, Product> getTable() {
    	return this.productsTable;
    }
}
