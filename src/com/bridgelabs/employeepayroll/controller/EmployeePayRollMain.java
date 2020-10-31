package com.bridgelabs.employeepayroll.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabs.employeepayroll.model.*;

public class EmployeePayRollMain {
	public List<EmployeePayRoll> employeePayRollList;
	public static Logger LOG = LogManager.getLogger(EmployeePayRollMain.class);
	public static BufferedReader consoleReader;

	// no-arg constructor initializes employeePayRollList, consoleReader
	public EmployeePayRollMain() {
		employeePayRollList = new ArrayList<EmployeePayRoll>();
		consoleReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// parameterized constructor initializes consoleReader
	public EmployeePayRollMain(List<EmployeePayRoll> employeePayRollList) {
		this.employeePayRollList = employeePayRollList;
		consoleReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// enum for different input output services
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO
	}

	public static void main(String[] args) {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		try {
			LOG.info("Welcome to Employee Payroll System!\n");
			int choice;
			do {
				LOG.info(
						"Chooose one option:\n1. Read and add employee payRoll details from console\n2. Write employee payRoll details to console\n3. Wrie employee payRoll details to a file\n4. Print details from file\n5. Find no. of entries in file\n6. Read employee payRoll details from the file\n7. Exit\n");
				try {
					choice = Integer.parseInt(consoleReader.readLine());
				} catch (NumberFormatException | IOException e) {
					throw new EmployeePayRollException("Choice should be a number!");
				}
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
					LOG.info("Thanks for using our system!");
					break;
				default:
					LOG.info("Please enter valid choice!\n\n");
					break;
				}
			} while (choice != 7);
		} catch (EmployeePayRollException e) {
			LOG.error(e.getMessage());
		}
	}

	// reads employee payroll details from console or file or database
	public void readEmployeePayRollDetails(IOService ioService) throws EmployeePayRollException {
		try {
			if (ioService.equals(IOService.CONSOLE_IO)) {
				LOG.info("Enter the employee Id: \n");
				String id = consoleReader.readLine();
				LOG.info("Enter the employee name: \n");
				String name = consoleReader.readLine();
				LOG.info("Enter the employee salary: \n");
				Long salary = Long.parseLong(consoleReader.readLine());
				EmployeePayRoll employeePayRoll = new EmployeePayRoll(id, name, salary);
				employeePayRollList.add(employeePayRoll);
			}
			if (ioService.equals(IOService.FILE_IO))
				this.employeePayRollList = new EmployeePayRollIOService().readEmployeePayRollDetailsFromFile();
			else
				this.employeePayRollList = EmployeePayRollDBService.getInstance()
						.readEmployeePayRollDetailsFromDatabase();

		} catch (IOException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
	}

	// update employee payroll-details in database
	public void updateEmployeePayRollDetails(IOService ioService, Long basic_pay, String name)
			throws EmployeePayRollException {
		EmployeePayRollDBService.getInstance().updateBasicPayDetailsInDatabase(basic_pay, name);
	}

	// writes employee payroll details to console or file or database
	public void writeEmployeePayRollDetails(IOService ioService) throws EmployeePayRollException {
		if (ioService.equals(IOService.CONSOLE_IO))
			LOG.info(employeePayRollList.toString());
		if (ioService.equals(IOService.FILE_IO))
			new EmployeePayRollIOService().writeEmployeePayRollDetailsToFile(employeePayRollList);
	}

	// retrieves employee pay roll details who started between certain date range
	public List<EmployeePayRoll> getEmployeePayRollDataForParticularDates(String start, String end)
			throws EmployeePayRollException {
		return EmployeePayRollDBService.getInstance().retrieveEmployeePayRollDetails(start, end);
	}

	// counts no. of entries in file
	public long countEntries(IOService ioService) throws EmployeePayRollException {
		long entriesCount = 0;
		if (ioService.equals(IOService.FILE_IO)) {
			entriesCount = new EmployeePayRollIOService().countEntries();
		}
		LOG.info("No. of entries in the file: " + entriesCount);
		return entriesCount;
	}

	// prints employeePayRoll data from file
	public void printDetails(IOService ioService) throws EmployeePayRollException {
		if (ioService.equals(IOService.FILE_IO))
			new EmployeePayRollIOService().printDetails();
	}
}
