package com.bridgelabs.employeepayroll.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	// adds employee payroll object to database and employee pay roll list
	public void addEmployeePayRollDetailsToDB(EmployeePayRoll employeePayRoll) throws EmployeePayRollException {
		if (EmployeePayRollDBService.getInstance().addEmployeePayRollDetails(employeePayRoll))
			employeePayRollList.add(employeePayRoll);
	}

	// adds multiple employee payroll objects to database and employee pay roll list
	public void addEmployeePayRollDetailsToDBWithOutThread(List<EmployeePayRoll> empList) {
		empList.forEach(emp -> {
			LOG.info("Employee being added: " + emp.getEmpName());
			try {
				if (EmployeePayRollDBService.getInstance().addEmployeePayRollDetails(emp)) {
					employeePayRollList.add(emp);
					LOG.info("Employee added: " + emp.getEmpName());
				}
			} catch (EmployeePayRollException e) {
				e.printStackTrace();
			}
		});
		LOG.info(empList);
	}

	// adds multiple employee payroll objects to database and employee pay roll list
	public void addEmployeePayRollDetailsToDBWithThreads(List<EmployeePayRoll> empList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
		empList.forEach(emp -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(emp.hashCode(), false);
				LOG.info("Employee being added: " + Thread.currentThread().getName());
				try {
					if (EmployeePayRollDBService.getInstance().addEmployeePayRollDetails(emp)) {
						employeePayRollList.add(emp);
						employeeAdditionStatus.put(emp.hashCode(), true);
						LOG.info("Employee added: " + Thread.currentThread().getName());
					}
				} catch (EmployeePayRollException e) {
					e.printStackTrace();
				}
			};
			Thread thread = new Thread(task, emp.getEmpName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage());
			}
		}
		LOG.info(empList);
	}

	
	// delete employee pay roll from payroll table and from list
	public void deleteEmployeePayRollFromPayRollTableAndList(String name) throws EmployeePayRollException {
		EmployeePayRollDBService.getInstance().deleteEmployeePayRollFromPayRollTable(name);
		employeePayRollList = employeePayRollList.stream().filter(emp -> !(emp.getEmpName().equals(name)))
				.collect(Collectors.toList());
	}

	// retrieves employee pay roll details who started between certain date range
	public List<EmployeePayRoll> getEmployeePayRollDataForParticularDates(String start, String end)
			throws EmployeePayRollException {
		return EmployeePayRollDBService.getInstance().retrieveEmployeePayRollDetails(start, end);
	}

	// returns the average salary by gender from database
	public long getAvgSalaryByGenderFromDB(char gender) throws EmployeePayRollException {
		return EmployeePayRollDBServiceFunc.getInstance().getAvgSalaryByGender(gender);
	}

	// returns the sum of salary by gender from database
	public long getTotalSalaryByGenderFromDB(char gender) throws EmployeePayRollException {
		return EmployeePayRollDBServiceFunc.getInstance().getTotalSalaryByGender(gender);
	}

	// returns no of employees in database by gender
	public long getCountOfEmployeesByGenderFromDB(char gender) throws EmployeePayRollException {
		return EmployeePayRollDBServiceFunc.getInstance().getCountOfEmployeesByGender(gender);
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
