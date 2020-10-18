package com.bridgelabs.employeepayroll.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabs.employeepayroll.model.*;

public class EmployeePayRollMain {
	public List<EmployeePayRoll> employeePayRollList;
	public static BufferedWriter consoleWriter;
	public static BufferedReader consoleReader;

	public EmployeePayRollMain() {
		employeePayRollList = new ArrayList<EmployeePayRoll>();
		consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		consoleReader = new BufferedReader(new InputStreamReader(System.in));
	}

	public EmployeePayRollMain(List<EmployeePayRoll> employeePayRollList) {
		this.employeePayRollList = employeePayRollList;
		consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		consoleReader = new BufferedReader(new InputStreamReader(System.in));
	}

	public enum IOService {
		CONSOLE_IO, FILE_IO
	}

	public static void main(String[] args) {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		try {
			consoleWriter.write("Welcome to Employee Payroll System!\n");
			consoleWriter.flush();
			int choice;
			do {
				consoleWriter.write(
						"Chooose one option:\n1. Read and add employee payRoll details from console\n2. Write employee payRoll details to console\n3. Wrie employee payRoll details to a file\n4. Print details from file\n5. Find no. of entries in file\n6. Read employee payRoll details from the file\n7. Exit\n");
				consoleWriter.flush();
				choice = Integer.parseInt(consoleReader.readLine());
				switch (choice) {
				case 1:
					employeePayRollMain.readEmployeePayRollDetails(IOService.CONSOLE_IO);
					break;
				case 2:
					employeePayRollMain.writeEmployeePayRollDetails(IOService.CONSOLE_IO);
					break;
				case 3:
					employeePayRollMain.writeEmployeePayRollDetails(IOService.FILE_IO);
					break;
				case 4:
					if (employeePayRollMain.countEntries(IOService.FILE_IO) == 0)
						employeePayRollMain.writeEmployeePayRollDetails(IOService.FILE_IO);
					employeePayRollMain.printDetails(IOService.FILE_IO);
					break;
				case 5:
					employeePayRollMain.countEntries(IOService.FILE_IO);
					break;
				case 6:
					employeePayRollMain.readEmployeePayRollDetails(IOService.FILE_IO);
					break;
				case 7:
					consoleWriter.write("Thanks for using our system!");
					consoleWriter.flush();
					break;
				default:
					consoleWriter.write("Please enter valid choice!\n\n");
					consoleWriter.flush();
					break;
				}
			} while (choice != 7);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				consoleWriter.close();
				consoleReader.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	// reads employee payroll details from console
	public void readEmployeePayRollDetails(IOService ioService) throws IOException {
		if (ioService.equals(IOService.CONSOLE_IO)) {
			consoleWriter.write("Enter the employee Id: \n");
			consoleWriter.flush();
			String id = consoleReader.readLine();
			consoleWriter.write("Enter the employee name: \n");
			consoleWriter.flush();
			String name = consoleReader.readLine();
			consoleWriter.write("Enter the employee salary: \n");
			consoleWriter.flush();
			Long salary = Long.parseLong(consoleReader.readLine());
			EmployeePayRoll employeePayRoll = new EmployeePayRoll(id, name, salary);
			employeePayRollList.add(employeePayRoll);
		} else {
			this.employeePayRollList = new EmployeePayRollService().readEmployeePayRollDetailsFromFile();
		}
	}

	// writes employee payroll details to console
	public void writeEmployeePayRollDetails(IOService ioService) throws IOException {
		if (ioService.equals(IOService.CONSOLE_IO))
			consoleWriter.write(employeePayRollList.toString());
		else
			new EmployeePayRollService().writeEmployeePayRollDetailsToFile(employeePayRollList);
	}

	// counts no. of entries
	public long countEntries(IOService ioService) throws IOException {
		long entriesCount = 0;
		if (ioService.equals(IOService.FILE_IO)) {
			entriesCount = new EmployeePayRollService().countEntries();
		}
		consoleWriter.write("No. of entries in the file: " + entriesCount + "\n");
		consoleWriter.flush();
		return entriesCount;
	}

	// prints employeePayRoll data
	public void printDetails(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			new EmployeePayRollService().printDetails();
	}
}
