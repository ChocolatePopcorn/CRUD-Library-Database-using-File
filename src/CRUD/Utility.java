package CRUD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Year;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

public class Utility {
    static Scanner sc = new Scanner(System.in);

    public static void mainMenu() {
		System.out.println("Database Perpustakaan");
		System.out.println("1. Show all book");
		System.out.println("2. Find a book");
		System.out.println("3. Add a book");
		System.out.println("4. Update a book");
		System.out.println("5. Delete a book");
		System.out.print(">> ");
	}
	
	private static String capitalized(String input) {
		String lowerCaseInput = input.toLowerCase();
		String[] splitted = lowerCaseInput.split(" ");

		String capitalized = "";
		for(String item : splitted) {
			capitalized += " " + item.toUpperCase().charAt(0) + item.substring(1);
		}

		return capitalized;
	}

	static void printData(String data, int countBook) {
		String[] splittedData = data.split(",");
		String name = splittedData[2]; 
		String publisher = splittedData[3]; 
		String title = splittedData[4]; 

		String capitalizedName = capitalized(name);
		String capitalizedPublisher = capitalized(publisher);
		String capitalizedTitle = capitalized(title);
		
		System.out.printf("| %-2s | %-5s | %-30s | %-30s | %-30s |\n", countBook, splittedData[1], capitalizedName, capitalizedPublisher, capitalizedTitle);
	}

	public static void clearScreen() {
		try {
			if(System.getProperty("os.name").contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}
			else {
				System.out.println("\033\143");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String checkValidYear(String inputtedYear) {
		String year = inputtedYear;
		boolean checkYear = false;
		while(checkYear == false) {
			try {
				Year.parse(year);
				checkYear = true;
			} catch (Exception e) {
				System.err.println("Invalid year inputted!");
				System.out.print("Enter the published year: ");
				checkYear = false;
				year = sc.nextLine();
			}
		}
		return year;
	}

	static void renameFile() {
		//delete database.txt
		File deleteFile = new File("database.txt");
		deleteFile.delete();
		
		//rename the databaseTemp.txt -> database.txt
		File newFile = new File("databaseTemp.txt");
		File rename = new File("database.txt");

		//rename file
		newFile.renameTo(rename);
	}

	static String skippedValidation(String current) {
		String inputted = sc.nextLine();
		if(inputted.startsWith("-")) return current;
		else return inputted;
	}
 
    static int counterBookNumber(String name, String year) {
		int counter = 0;
		try(FileReader reader = new FileReader("database.txt");
			BufferedReader buffReader = new BufferedReader(reader)) {
			String data;
			while((data = buffReader.readLine()) != null) {
				if(data.toLowerCase().contains(name.toLowerCase()) && data.contains(year)) {
					counter++;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return counter;
	}

	public static void sortDataByYear() {
		try (FileReader fileRead = new FileReader("database.txt");
			FileWriter fileWrite = new FileWriter("databaseTemp.txt");
			BufferedReader buffReader = new BufferedReader(fileRead);
			BufferedWriter buffWriter = new BufferedWriter(fileWrite)) {
			
			Vector<String> dbVector = new Vector<String>();
			
			String data = "";
			while((data = buffReader.readLine()) != null) {
				String[] split = data.split(",");
				String template = split[1] +  "," + split[0] + "," + split[2] + "," + split[3] + "," + split[4];
				dbVector.add(template);
			}

			Collections.sort(dbVector);

			boolean formattedLine = false;
			for(var reverse : dbVector) {
				String[] splitRev = reverse.split(",");
				String tempRev = splitRev[1] +  "," + splitRev[0] + "," + splitRev[2] + "," + splitRev[3] + "," + splitRev[4];
				if(!formattedLine) {
					buffWriter.write(tempRev);
					formattedLine = true;
				}
				else buffWriter.write("\n" + tempRev);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		renameFile();
	}

}
