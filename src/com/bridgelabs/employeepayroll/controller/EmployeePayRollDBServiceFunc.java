package com.bridgelabs.employeepayroll.controller;

import java.sql.*;

import com.bridgelabs.employeepayroll.connector.PayRollDatabaseConnector;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

public class EmployeePayRollDBServiceFunc {
	private static PreparedStatement empStatement;
	private static ResultSet resultSet;
	private static EmployeePayRollDBServiceFunc employeePayRollDBServiceFunc;

	// no-arg constructor made private to ensure singleton property
	private EmployeePayRollDBServiceFunc() {
	}

	// creates new instance if null else returns the same instance
	public static EmployeePayRollDBServiceFunc getInstance() {
		if (employeePayRollDBServiceFunc == null)
			employeePayRollDBServiceFunc = new EmployeePayRollDBServiceFunc();
		return employeePayRollDBServiceFunc;
	}

	// returns average salary by gender
	public long getAvgSalaryByGender(char gender) throws EmployeePayRollException {
		long avgSalary = 0l;
		try (Connection con = PayRollDatabaseConnector.getConnection()) {
			empStatement = con.prepareStatement(
					"select e.gender,avg(p.basic_pay) as average_pay from employee e join payroll p on e.employee_id=p.employee_id where e.gender= ? and e.is_active=true");
			empStatement.setString(1, String.valueOf(gender));
			resultSet = empStatement.executeQuery();
			while (resultSet.next())
				avgSalary = resultSet.getLong("average_pay");
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return avgSalary;
	}

	// returns total salary by gender
	public long getTotalSalaryByGender(char gender) throws EmployeePayRollException {
		long salary = 0l;
		try (Connection con = PayRollDatabaseConnector.getConnection()) {
			empStatement = con.prepareStatement(
					"select e.gender,sum(basic_pay) as total_pay from employee e join payroll p on e.employee_id=p.employee_id where e.gender=? and e.is_active=true");
			empStatement.setString(1, String.valueOf(gender));
			resultSet = empStatement.executeQuery();
			while (resultSet.next())
				salary = resultSet.getLong("total_pay");
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return salary;
	}

	// returns count of employees by gender
	public long getCountOfEmployeesByGender(char gender) throws EmployeePayRollException {
		long count = 0l;
		try (Connection con = PayRollDatabaseConnector.getConnection()) {
			empStatement = con.prepareStatement(
					"select gender,count(gender) as count from employee where gender=? and is_active=true");
			empStatement.setString(1, String.valueOf(gender));
			resultSet = empStatement.executeQuery();
			while (resultSet.next())
				count = resultSet.getLong("count");
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return count;
	}
}
