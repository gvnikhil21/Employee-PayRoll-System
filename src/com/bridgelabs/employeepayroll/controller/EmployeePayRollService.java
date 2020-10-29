package com.bridgelabs.employeepayroll.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabs.employeepayroll.connector.PayRollDatabaseConnector;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;

public class EmployeePayRollService {
	private static final String PATH_FILE = "D:\\AssignmentBridgeLabs\\Employee PayRoll System\\employeePayRollFile.txt";

	// writes details to employeePayRolllDetails.txt file
	public void writeEmployeePayRollDetailsToFile(List<EmployeePayRoll> employeePayRollList) {
		StringBuilder empBuilder = new StringBuilder();
		employeePayRollList.stream().forEach(emp -> {
			empBuilder.append(emp + "\n");
		});
		try {
			Files.write(Paths.get(PATH_FILE), empBuilder.toString().getBytes());
			EmployeePayRollMain.LOG.info("Details saved successfully to employeePayRollDetails.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// counts entries in employeePayRolllDetails.txt file
	public long countEntries() {
		try {
			return Files.lines(Paths.get(PATH_FILE)).count();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// prints the details in employeePayRolllDetails.txt file
	public void printDetails() {
		try {
			StringBuilder empBuilder = new StringBuilder();
			Files.lines(Paths.get(PATH_FILE)).forEach(e -> {
				empBuilder.append(e + "\n");
			});
			EmployeePayRollMain.LOG.info(empBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// read data from employeePayRolllDetails.txt file
	public List<EmployeePayRoll> readEmployeePayRollDetailsFromFile() throws IOException {
		List<EmployeePayRoll> empList = new ArrayList<EmployeePayRoll>();
		Files.lines(Paths.get(PATH_FILE)).forEach(line -> {
			line = line.replace("Employee ID: ", "");
			line = line.replace(" Employee Name: ", ",");
			line = line.replace(" Employee Salary: ", ",");
			String empData[] = line.toString().trim().split(",");
			empList.add(new EmployeePayRoll(empData[0], empData[1], Long.parseLong(empData[2])));
		});
		EmployeePayRollMain.LOG.info("Details read successfully from employeePayRollDetails.txt");
		return empList;
	}

	public List<EmployeePayRoll> readEmployeePayRollDetailsFromDatabase() {
		List<EmployeePayRoll> empList = new ArrayList<EmployeePayRoll>();
		Connection con = PayRollDatabaseConnector.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"select employee.employee_id, employee.name, basic_pay from employee join payroll on employee.employee_id=payroll.employee_id");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				empList.add(new EmployeePayRoll(rs.getString("employee_id"), rs.getString("name"),
						rs.getLong("basic_pay")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EmployeePayRollMain.LOG.info("Details read successfully from payroll_service database");
		return empList;
	}
}
