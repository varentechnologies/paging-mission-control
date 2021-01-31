package com.example.pagingmissioncontrol.dataprocessor;

import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class InputProcessor {

	private boolean isCustomFile = false;
	
	public String getFile() {
		boolean flag = true;
		System.out.println("\n*************************************************************");
		System.out.println("Welcome to Paging Mission Control! Let's Get Started.");
		System.out.println("*************************************************************\n");
		String customFileName = "data.txt";
		
		do {
			Scanner scanner = new Scanner(System.in); // Create a Scanner object
			System.out.println("Please select an option.");
			System.out.println(
					"To read from default file(/paging-mission-control/src/main/resources/data.txt), Enter \"1\".");
			System.out.println("To read from custom file by giving filepath, Enter \"2\".");
			System.out.println("To exit and quit enter \"q\"");

			String userOptionSelection = scanner.nextLine(); // Read user input
			switch (userOptionSelection) {
			case "1":
				scanner.close();
				flag = false;
				break;
			case "2":
				System.out.println("Please enter the filepath to the file, including the name of file.");
				customFileName = scanner.nextLine();
				System.out.println("User entered filePath is: " + customFileName);
				scanner.close();
				this.setCustomFile(true);
				flag = false;
				break;
			case "q":
				scanner.close();
				System.exit(0);
			default:
				System.out.println("Incorrect option selected. Please try again.");
			}
		} while (flag);

		return customFileName;
	}

	public boolean getIsCustomFile() {
		return isCustomFile;
	}

	public void setCustomFile(boolean isCustomFile) {
		this.isCustomFile = isCustomFile;
	}
	
}
