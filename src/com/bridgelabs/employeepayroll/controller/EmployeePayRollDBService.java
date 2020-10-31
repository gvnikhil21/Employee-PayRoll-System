package com.bridgelabs.employeepayroll.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabs.employeepayroll.connector.PayRollDatabaseConnector;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

public class EmployeePayRollDBService {
	private static Connection con;
	private static PreparedStatement empStatement;
	private static EmployeePayRollDBService employeePayRollDBService;

	// no-arg constructor made private to ensure singleton property
	private EmployeePayRollDBService() {
		try {
			con = PayRollDatabaseConnector.getConnection();
		} catch (EmployeePayRollException e) {
			EmployeePayRollMain.LOG.error(e.getMessage());
		}
	}

	// creates new instance if null else returns the same instance
	public static EmployeePayRollDBService getInstance() {
		if (employeePayRollDBService == null)
			employeePayRollDBService = new EmployeePayRollDBService();
		return employeePayRollDBService;
	}

	// reads employee payRoll details from database
	public List<EmployeePayRoll> readEmployeePayRollDetailsFromDatabase() throws EmployeePayRollException {
		List<EmployeePayRoll> empList = new ArrayList<EmployeePayRoll>();
		try {
			empStatement = con.prepareStatement(
					"select employee.employee_id, employee.name, basic_pay from employee join payroll on employee.employee_id=payroll.employee_id");
			ResultSet rs = empStatement.executeQuery();
			while (rs.next()) {
				empList.add(new EmployeePayRoll(rs.getString("employee_id"), rs.getString("name"),
						rs.getLong("basic_pay")));
			}
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		EmployeePayRollMain.LOG.info("Details read successfully from database");
		return empList;
	}

	// updates details in database
	public void updateBasicPayDetailsInDatabase() throws EmployeePayRollException {
		try {
			String query = "update employee join payroll on employee.employee_id= payroll.employee_id set basic_pay=? where employee.name=?";
			empStatement = con.prepareStatement(query);
			empStatement.setLong(1, 3000000);
			empStatement.setString(2, "Terissa");
			int status = empStatement.executeUpdate();
			if (status > 0)
				EmployeePayRollMain.LOG.info("Details updated successfully to database");
			else
				EmployeePayRollMain.LOG.info("Details not updated");
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
	}
}
