package com.bridgelabs.employeepayroll.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.bridgelabs.employeepayroll.model.EmployeePayRoll;

public class EmployeePayRollService {
	private static final String PATH_FILE = "D:\\AssignmentBridgeLabs\\Employee PayRoll System\\employeePayRollFile.txt";

	public void writeEmployeePayRollDetailsToFile(List<EmployeePayRoll> employeePayRollList) {
		StringBuilder empBuilder = new StringBuilder();
		employeePayRollList.stream().forEach(emp -> {
			empBuilder.append(emp + "\n");
		});
		try {
			Files.write(Paths.get(PATH_FILE), empBuilder.toString().getBytes());
			EmployeePayRollMain.consoleWriter.write("Details saved successfully to employeePayRollDetails.txt\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long countEntries() {
		try {
			return Files.lines(Paths.get(PATH_FILE)).count();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void printDetails() {
		try {
			StringBuilder empBuilder = new StringBuilder();
			Files.lines(Paths.get(PATH_FILE)).forEach(e -> {
				empBuilder.append(e + "\n");
			});
			EmployeePayRollMain.consoleWriter.write(empBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
