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
	private static ResultSet resultSet;
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
			empList = getDataInDB();
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		EmployeePayRollMain.LOG.info("Details read successfully from database");
		return empList;
	}

	// updates details in database
	public void updateBasicPayDetailsInDatabase(Long basic_pay, String name) throws EmployeePayRollException {
		try {
			String query = "update employee e join payroll p on e.employee_id= p.employee_id set basic_pay=? where e.name=?";
			empStatement = con.prepareStatement(query);
			empStatement.setLong(1, basic_pay);
			empStatement.setString(2, name);
			int status = empStatement.executeUpdate();
			if (status > 0)
				EmployeePayRollMain.LOG.info("Details updated successfully to database");
			else
				EmployeePayRollMain.LOG.info("Details not updated");
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
	}

	// retrieve all employee pay-roll details who started between certain date range
	public List<EmployeePayRoll> retrieveEmployeePayRollDetails(String start, String end)
			throws EmployeePayRollException {
		List<EmployeePayRoll> empList = new ArrayList<EmployeePayRoll>();
		try {
			empStatement = con.prepareStatement(
					"select * from employee e join payroll p on e.employee_id=p.employee_id where start_date between cast(? as date) and cast(? as date)");
			empStatement.setString(1, start);
			empStatement.setString(2, end);
			empList = getDataInDB();
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return empList;
	}

	// returns employee payroll data in database
	private List<EmployeePayRoll> getDataInDB() throws EmployeePayRollException {
		List<EmployeePayRoll> empList = new ArrayList<EmployeePayRoll>();
		try {
			resultSet = empStatement.executeQuery();
			while (resultSet.next()) {
				empList.add(new EmployeePayRoll(resultSet.getString("employee_id"), resultSet.getString("name"),
						resultSet.getLong("basic_pay")));
			}
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return empList;
	}
}
