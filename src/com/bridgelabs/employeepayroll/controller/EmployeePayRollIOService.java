package com.bridgelabs.employeepayroll.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.bridgelabs.employeepayroll.model.EmployeePayRoll;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

public class EmployeePayRollIOService {
	private static final String PATH_FILE = "D:\\AssignmentBridgeLabs\\Employee PayRoll System\\employeePayRollFile.txt";

	// writes details to employeePayRolllDetails.txt file
	public void writeEmployeePayRollDetailsToFile(List<EmployeePayRoll> employeePayRollList)
			throws EmployeePayRollException {
		StringBuilder empBuilder = new StringBuilder();
		employeePayRollList.stream().forEach(emp -> empBuilder.append(emp + "\n"));
		try {
			Files.write(Paths.get(PATH_FILE), empBuilder.toString().getBytes());
			EmployeePayRollMain.LOG.info("Details saved successfully to employeePayRollDetails.txt");
		} catch (IOException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
	}

	// counts entries in employeePayRolllDetails.txt file
	public long countEntries() throws EmployeePayRollException {
		try (Stream<String> input = Files.lines(Paths.get(PATH_FILE))) {
			return input.count();
		} catch (IOException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
	}

	// prints the details in employeePayRolllDetails.txt file
	public void printDetails() throws EmployeePayRollException {
		try (Stream<String> input = Files.lines(Paths.get(PATH_FILE))) {
			StringBuilder empBuilder = new StringBuilder();
			input.forEach(e -> empBuilder.append(e + "\n"));
			EmployeePayRollMain.LOG.info(empBuilder);
		} catch (IOException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
	}

	// read data from employeePayRolllDetails.txt file
	public List<EmployeePayRoll> readEmployeePayRollDetailsFromFile() throws EmployeePayRollException {
		List<EmployeePayRoll> empList = new ArrayList<>();
		try (Stream<String> input = Files.lines(Paths.get(PATH_FILE))) {
			input.forEach(line -> {
				line = line.replace("Employee ID: ", "");
				line = line.replace(" Employee Name: ", ",");
				line = line.replace(" Employee Salary: ", ",");
				String[] empData = line.trim().split(",");
				empList.add(new EmployeePayRoll(empData[0], empData[1], Long.parseLong(empData[2])));
			});
		} catch (IOException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		EmployeePayRollMain.LOG.info("Details read successfully from employeePayRollDetails.txt");
		return empList;
	}
}
