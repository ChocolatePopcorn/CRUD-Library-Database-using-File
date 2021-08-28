package CRUD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Operation {
    static Scanner sc = new Scanner(System.in);

    public static void showBook() {
		try (FileReader fileRead = new FileReader("database.txt"); 
			BufferedReader inputReader = new BufferedReader(fileRead)) {
			
			String data;
			int countBook = 1;
			System.out.println("-----------------------------------------------------------------------------------------------------------------");
			System.out.println("| No | Year  | Author			      | Publisher		       | Title				|");
			System.out.println("-----------------------------------------------------------------------------------------------------------------");
			
			while((data = inputReader.readLine()) != null) {
				Utility.printData(data, countBook++);
			}
	
			System.out.println("-----------------------------------------------------------------------------------------------------------------");

		} catch (Exception e) {
			System.err.println("File not found!");
			e.printStackTrace();
		}	
	}
	
	public static void findBook() {

		try (FileReader fileCheck = new FileReader("database.txt");
			BufferedReader reader = new BufferedReader(fileCheck)) {
			sc = new Scanner(System.in);

			System.out.print("Enter a keyword: ");
			String inputUser = sc.nextLine();

			String[] keywords = inputUser.split(" ");
			// System.out.println(Arrays.toString(keywords));

			String data;
			int countBook = 1;
			boolean checkTrue; //check the keywords inputted from user to compare with database
			boolean checkNotFound = true; //find book (is exist / not exist)

			System.out.println("-----------------------------------------------------------------------------------------------------------------");
			System.out.println("| No | Year  | Author			      | Publisher		       | Title				|");
			System.out.println("-----------------------------------------------------------------------------------------------------------------");

			while((data = reader.readLine()) != null) { //looping from database
				checkTrue = true;
				for(var keyword: keywords) {
					checkTrue = checkTrue && data.toLowerCase().contains(keyword.toLowerCase());
				}
				
				if(checkTrue) {
					checkNotFound = false;
					Utility.printData(data, countBook++);
				}
			}

			if(checkNotFound) System.err.println("Keywords Not found!");

			System.out.println("-----------------------------------------------------------------------------------------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteBook() {
		try(FileReader fileRead = new FileReader("database.txt");
		FileWriter fileWrite = new FileWriter("databaseTemp.txt");
		BufferedReader buffReader = new BufferedReader(fileRead);
		BufferedWriter buffWriter = new BufferedWriter(fileWrite)) {
			
			showBook();
			System.out.print("Enter the keywords: ");
			String inputted = sc.nextLine();
			String[] keywords = inputted.split(" ");
			
			String data;
			boolean checkTrue;
			boolean checkPrint = true;
			boolean checkDeleted = false;

			while((data = buffReader.readLine()) != null) {
				checkTrue = true;
				for(var keyword: keywords) {
					checkTrue = checkTrue && data.toLowerCase().contains(keyword.toLowerCase());
				}

				if(checkTrue) { //if keyword exist with database, skip the item 
					checkDeleted = true;
					continue;
				}
				else { //write to databaseTemp
					if(checkPrint) { //formatting for write
						checkPrint = false;
						buffWriter.write(data);
					}
					else {
						buffWriter.write("\n" + data);
					}
				}
			}

			if(checkDeleted == false) System.err.println("Data not Found!");
			else if(checkDeleted) System.err.println("Data Deleted Successfully!");


		} catch (Exception e) {
			e.printStackTrace();
		}

		Utility.renameFile();
	}

	public static void updateBook() {
		showBook();
		System.out.print("Enter the keywords: ");
		String input = sc.nextLine();
		boolean checkUpdatedData = true;

		String[] keywords = input.split(" ");

		try (FileReader fileRead = new FileReader("database.txt");
			FileWriter fileWrite = new FileWriter("databaseTemp.txt");
			BufferedReader buffReader = new BufferedReader(fileRead);
			BufferedWriter buffWriter = new BufferedWriter(fileWrite)) {

			String data;
			boolean checkData; //check the exists data in database while looping
			boolean checkerDataOutput = false; //to formating the write
			boolean checkUpdated = false; //to check updated data
			while((data = buffReader.readLine()) != null) {
				checkData = true;
				for(var keyword : keywords) {
					checkData = checkData && data.toLowerCase().contains(keyword.toLowerCase());
				}
				
				if(checkData) { //if data exists
					String title, name, publisher, year, inputtedYear;
					checkUpdated = true;

					String[] splitCurrData = data.split(",");
					String currYear = splitCurrData[1];
					String currName = splitCurrData[2];
					String currPublisher = splitCurrData[3];
					String currTitle = splitCurrData[4];
					String[] splitNumber = splitCurrData[0].split("_");
					int currBookNumber = Integer.valueOf(splitNumber[2]);

					System.err.println("Data Found!");
					System.out.println("Enter '-' to skip!"); 

					System.out.print("Enter a title do you want to update: ");
					title = Utility.skippedValidation(currTitle);

					System.out.print("Enter the author of the book do you want to update: ");
					name = Utility.skippedValidation(currName);

					System.out.print("Enter the publisher name do you want to update: ");
					publisher = Utility.skippedValidation(currPublisher);

					System.out.print("Enter the published year do you want to update: ");
					inputtedYear = sc.nextLine();

					if(inputtedYear.startsWith("-")) year = currYear;
					else year = Utility.checkValidYear(inputtedYear);

					//check if there's no updated data (got skipped)
					if(title.equals(currTitle) && name.equals(currName) && publisher.equals(currPublisher) && year.equals(currYear)) checkUpdatedData = false;
					
					int count = 0;
					if(checkUpdatedData == false) count = currBookNumber;
					else if(checkUpdatedData) count = Utility.counterBookNumber(name, year) + 1;
					
					String tempName = "";
					String[] splitted = name.split(" ");
					for(String item : splitted) {
						tempName += item;
					}

					String newData = tempName + "_" + year + "_" + count + "," + year + "," + name + "," + publisher + "," + title;
					
					if(checkerDataOutput) { //if there's data exist in database
						buffWriter.write("\n" + newData);
					}
					else { //if there's not exist data in database
						buffWriter.write(newData);
						checkerDataOutput = true;
					}
				}
				else {
					if(checkerDataOutput) { //if there's data exist in database
						buffWriter.write("\n" + data);
					}
					else { //if there's not exist data in database
						buffWriter.write(data);
						checkerDataOutput = true;
					} 
				}
			}

			if(checkUpdated == false) System.err.println("Failed to Update!");
			else if(checkUpdated) System.err.println("Data Updated Successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		Utility.renameFile();		
	}

	public static void addBook() {
		System.out.print("Enter a title: ");
		String title = sc.nextLine();

		System.out.print("Enter the author of the book: ");
		String name = sc.nextLine();

		System.out.print("Enter the publisher name: ");
		String publisher = sc.nextLine();

		System.out.print("Enter the published year: ");
		String inputtedYear = sc.nextLine();
		String year = Utility.checkValidYear(inputtedYear); 

		try (FileReader fileRead = new FileReader("database.txt");
			FileWriter fileWrite = new FileWriter("database.txt", true); //append
			BufferedReader buffReader = new BufferedReader(fileRead);
			BufferedWriter buffWriter = new BufferedWriter(fileWrite)) {
			
			int counter = Utility.counterBookNumber(name, year) + 1;
			
			String temp = "";
			String[] splitted = name.split(" ");
			for(String item : splitted) {
				temp += item;
			}
			
			if(counter > 1) temp += "_" + year + "_" + counter + "," + year + "," + name + "," + publisher + "," + title;
			else temp += "_" + year + "_1," + year + "," + name + "," + publisher + "," + title;  

			buffWriter.write("\n" + temp);
			System.out.println("Success add a new book!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
