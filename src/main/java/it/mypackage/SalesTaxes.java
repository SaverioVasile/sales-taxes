package it.mypackage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class SalesTaxes {

	public static void main(String[] args) {
		
		Scanner scanner = null;
		Stream<String> lines = null;
		
		try {
			System.out.println("Insert the path of the input file: ");
			scanner = new Scanner(System.in);
			String path = scanner.nextLine(); 	
			String inputFile = "\\input.txt";
			String outputFile = "\\output.txt";		
			File file = new File(path + inputFile);
			
			if (file.exists()) {
				System.out.println("Input file found");
				lines = Files.lines(file.toPath()).filter(line -> !line.isEmpty());
				
				ArrayList<String> entryList = (ArrayList<String>) lines.collect(Collectors.toList());
				System.out.println("Validating the input");
				boolean hasError = validateInput(entryList);
				if (!hasError) {
					System.out.println("Input valid");
					ArrayList<Input> data = (ArrayList<Input>) entryList.stream().map(s -> getInputEntry(s.trim())).collect(Collectors.toList());
		
					System.out.println("Connecting to datasource");
					Datasource ds = new Datasource(path);
					HashMap<String, Product> table = ds.getTable();
					
					System.out.println("Calculating....");
					Output output = calculate(data, table);
					System.out.println("Creating output file");
					createOutputFile(path + outputFile, output);
					System.out.println("Process completed");
				} else {
					System.out.println("Input not valid");
				}
			} else {
				System.out.println("Input file not found");
			}; 
		} catch (IOException e) {
			System.err.println("IO Error: ");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println(e);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			System.err.println("Error while reading Datasource file: ");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Generic Error: ");
			e.printStackTrace();
		} finally {
			scanner.close();
			lines.close();
		}
	}

	private static boolean validateInput(ArrayList<String> entryList) {
		
		return entryList.stream().anyMatch(entry -> checkEntry(entry.trim()));		
	}

	private static boolean checkEntry(String entry) {
		
		String first = entry.substring(0, entry.indexOf(" "));
		String last = entry.substring(entry.lastIndexOf(" ") + 1, entry.length());
		if (!NumberUtils.isNumber(first) || !entry.contains(" at ") || !NumberUtils.isNumber(last)) {
			System.out.println("Error in line: " + entry);
			return true;
		} 
		
		return false;
	}

	private static Input getInputEntry(String entry) {
		
		Boolean isImported = Boolean.FALSE;
		if (StringUtils.isNotBlank(entry) && entry.contains("imported ")) {
			isImported = Boolean.TRUE;
			entry = entry.replace("imported ", "");
		}
		
		String[] splitted = Arrays.stream(entry.split(" at "))
		  .map(String::trim)
		  .toArray(String[]::new);
		
		Integer qty = Integer.valueOf((String) splitted[0].substring(0, splitted[0].indexOf(" ")));
		String item = splitted[0].substring(splitted[0].indexOf(" ")).trim();
		Double cost = Double.valueOf(splitted[1]);
		
		return new Input(qty, item, cost, isImported);			 
	}
	
	private static Output calculate(ArrayList<Input> data, HashMap<String, Product> table) {
		
		final Double[] salesTaxes = new Double[1];
		final Double[] totalCost = new Double[1];
		
		salesTaxes[0] = 0d;
		totalCost[0] = 0d;
		
		ArrayList<OutputEntry> outputEntryList = new ArrayList<OutputEntry>();
		
		data.stream().forEach(input -> {			
			String item = input.getItem();
			Boolean isTaxFree = table.get(item).getIsTaxFree();
			
			Double tax = 0d;
			Double entryTax = 0d;
			Double entryCost = input.getCost();
			if (!isTaxFree) {
				tax = input.getCost() * 0.1;
				entryCost = entryCost + tax;
				entryTax = entryTax + tax;
			} 
			
			if (input.getIsImported()) {
				tax = input.getCost() * 0.05;
				entryCost = entryCost + tax;
				entryTax = entryTax + tax;
			}
			
			entryCost = entryCost * input.getQty();
			entryTax = entryTax * input.getQty();
			
			entryCost = calculateRound(entryCost);
			entryTax = calculateRound(entryTax);
			
			salesTaxes[0] = salesTaxes[0] + entryTax;
			totalCost[0] = totalCost[0] + entryCost;
			
			OutputEntry outputEntry = new OutputEntry(input.getQty(), input.getItem(), entryCost, input.getIsImported());
			outputEntryList.add(outputEntry);
		});
		
		salesTaxes[0] = Math.round(salesTaxes[0] * 20.0) / 20.0;
		totalCost[0] = Math.round(totalCost[0] * 100.0) / 100.0;
				
		return new Output(outputEntryList, salesTaxes, totalCost);
	}

	private static Double calculateRound(Double value) {

		Double rounded = Math.round(value * 100) / 100.0;
		String rndString = String.format( "%.2f", rounded).replace(",", ".");
		char secondDecimalDigit = rndString.charAt(rndString.indexOf(".") + 2);
		int digit = Character.getNumericValue(secondDecimalDigit);
		if (digit > 0 && digit < 5) {
			digit = 5;
		} else if (digit > 5 && digit < 9) {
			digit = 9;
		} else {
			return rounded;
		}	

		rndString = rndString.substring(0, rndString.length()-1) + String.valueOf(digit);
		return Double.valueOf(rndString);
	}

	private static void createOutputFile(String path, Output output) throws IOException {
		
		ArrayList<OutputEntry> outputEntryList = output.getOutputEntryList();
		String[] line = new String[1];
		line[0] = "";
		DecimalFormat df = new DecimalFormat("0.00");
		outputEntryList.stream().forEach(outputEntry -> {
			line[0] = line[0] + String.valueOf(outputEntry.getQty()) + " ";
			if (outputEntry.getIsImported()) {
				line[0] = line[0] + "imported ";
			}
			line[0] = line[0] + String.valueOf(outputEntry.getItem()) + ": " +  df.format(outputEntry.getTaxedCost());
			line[0] = line[0] + System.lineSeparator();			
		});
		line[0] = line[0] + "Sales Taxes: " + df.format(output.getSalesTaxes()) + System.lineSeparator();
		line[0] = line[0] + "Total: " + df.format(output.getTotalCost()) + System.lineSeparator();
		line[0] = line[0].replace(",", ".");
		Files.write(Paths.get(path), line[0].getBytes());	
	}
}
