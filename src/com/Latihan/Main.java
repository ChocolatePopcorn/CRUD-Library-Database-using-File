package com.Latihan;

import java.io.IOException;
import java.util.Scanner;

import CRUD.Operation;
import CRUD.Utility;


public class Main {
	private Scanner sc;	
	public static void main(String[] args) throws IOException {
		new Main();
	}

	public Main() throws IOException {
		sc = new Scanner(System.in);
		String choose;
		boolean check = true;

		while(check) {
			Utility.clearScreen();
			Utility.sortDataByYear();
			Utility.mainMenu();
			choose = sc.next();

			switch(choose) {
				case "1":
					Utility.clearScreen(); 
					System.out.println("==============");
					System.out.println("Show Book Menu");
					System.out.println("==============");
					Operation.showBook();
					
					break;
				case "2":
					Utility.clearScreen();
					System.out.println("==============");
					System.out.println("Find Book Menu");
					System.out.println("==============");
					Operation.findBook();
					break;
				case "3":
					Utility.clearScreen();
					System.out.println("=============");
					System.out.println("Add Book Menu");
					System.out.println("=============");
					Operation.addBook();
					break;
				case "4":
					Utility.clearScreen();
					System.out.println("================");
					System.out.println("Update Book Menu");
					System.out.println("================");
					Operation.updateBook();
					break;
				case "5":
					Utility.clearScreen();
					System.out.println("================");
					System.out.println("Delete Book Menu");
					System.out.println("================");
					Operation.deleteBook();
					break;
				default:
					System.err.println("Invalid Input!\nPlease enter (1-5)!");
					break;
			}

			System.out.print("Do you want to continue (y/n)? ");
			choose = sc.next();
			
			while(!choose.equalsIgnoreCase("y") && !choose.equalsIgnoreCase("n")) {
				System.err.println("Invalid Input!\nPlease enter (y/n)");
				System.out.print("Do you want to continue (y/n)? ");
				choose = sc.next();
			}

			check = choose.equalsIgnoreCase("y");
		}
		sc.close();
	}
	
}
