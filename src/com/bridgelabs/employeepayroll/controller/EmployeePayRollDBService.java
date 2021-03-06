package com.bridgelabs.employeepayroll.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.util.*;

import com.bridgelabs.employeepayroll.connector.PayRollDatabaseConnector;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

public class EmployeePayRollDBService {
	private PreparedStatement empStatement;
	private ResultSet resultSet;
	private static EmployeePayRollDBService employeePayRollDBService;

	// no-arg constructor made private to ensure singleton property
	private EmployeePayRollDBService() {
	}

	// creates new instance if null else returns the same instance
	public static EmployeePayRollDBService getInstance() {
		if (employeePayRollDBService == null)
			employeePayRollDBService = new EmployeePayRollDBService();
		return employeePayRollDBService;
	}

	// adds payroll details to database
	public synchronized boolean addEmployeePayRollDetails(EmployeePayRoll employeePayRoll)
			throws EmployeePayRollException {
		Connection con = null;
		// adding to employee table
		try {
			con = PayRollDatabaseConnector.getConnection();
			con.setAutoCommit(false);
			empStatement = con.prepareStatement(
					"insert into employee (name,gender,start_date,company_id) values (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			empStatement.setString(1, employeePayRoll.getEmpName());
			empStatement.setString(2, String.valueOf(employeePayRoll.getGender()));
			empStatement.setDate(3, Date.valueOf(employeePayRoll.getStartDate()));
			empStatement.setString(4, employeePayRoll.getCompanyId());
			int rowAffected = empStatement.executeUpdate();
			if (rowAffected == 0)
				return false;
			if (rowAffected > 0) {
				resultSet = empStatement.getGeneratedKeys();
				if (resultSet.next())
					employeePayRoll.setEmpId(String.valueOf(resultSet.getInt(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				return false;
			} catch (SQLException e1) {
				throw new EmployeePayRollException(e1.getMessage());
			}
		}
		// adding to payroll table
		try {
			empStatement = con.prepareStatement(
					"insert into payroll (employee_id,basic_pay,deductions,taxable_pay,tax,net_pay) values (?,?,?,?,?,?)");
			BigDecimal basicPay = BigDecimal.valueOf(employeePayRoll.getEmpSalary());
			BigDecimal deductions = basicPay.multiply(BigDecimal.valueOf(0.2));
			BigDecimal taxablePay = basicPay.subtract(deductions);
			BigDecimal tax = taxablePay.multiply(BigDecimal.valueOf(0.1));
			BigDecimal netPay = taxablePay.subtract(tax);
			empStatement.setInt(1, Integer.valueOf(employeePayRoll.getEmpId()));
			empStatement.setBigDecimal(2, basicPay);
			empStatement.setBigDecimal(3, deductions);
			empStatement.setBigDecimal(4, taxablePay);
			empStatement.setBigDecimal(5, tax);
			empStatement.setBigDecimal(6, netPay);
			int rowAffected = empStatement.executeUpdate();
			if (rowAffected == 0)
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				return false;
			} catch (SQLException e1) {
				throw new EmployeePayRollException(e1.getMessage());
			}
		}
		// adding to employee_department table
		try {
			for (String deptId : employeePayRoll.getDepartment()) {
				empStatement = con
						.prepareStatement("insert into employee_department (employee_id,department_id) values (?,?)");
				empStatement.setInt(1, Integer.valueOf(employeePayRoll.getEmpId()));
				empStatement.setInt(2, Integer.valueOf(deptId));
				int rowsAffected = empStatement.executeUpdate();
				if (rowsAffected == 0)
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				return false;
			} catch (SQLException e1) {
				throw new EmployeePayRollException(e1.getMessage());
			}
		}
		// committing
		try {
			con.commit();
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw new EmployeePayRollException(e.getMessage());
			}
		}
		return true;
	}

	// reads employee payRoll details from database
	public List<EmployeePayRoll> readEmployeePayRollDetailsFromDatabase() throws EmployeePayRollException {
		List<EmployeePayRoll> empList = new ArrayList<>();
		try (Connection con = PayRollDatabaseConnector.getConnection()) {
			empStatement = con.prepareStatement(
					"select e.employee_id, e.name,e.gender, basic_pay from employee  e join payroll p on e.employee_id=p.employee_id where e.is_active=true");
			empList = getDataInDB();
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		EmployeePayRollMain.LOG.info("Details read successfully from database");
		return empList;
	}

	// updates details in database
	public synchronized boolean updateBasicPayDetailsInDatabase(Long basicPay, String name)
			throws EmployeePayRollException {
		int empId = -1;
		Connection con = null;
		con = PayRollDatabaseConnector.getConnection();
		try {
			con.setAutoCommit(false);
			String query = "update employee e join payroll p on e.employee_id= p.employee_id set basic_pay=? where e.name=?";
			empStatement = con.prepareStatement(query);
			empStatement.setLong(1, basicPay);
			empStatement.setString(2, name);
			int status = empStatement.executeUpdate();
			if (status == 0)
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				return false;
			} catch (SQLException e1) {
				throw new EmployeePayRollException(e.getMessage());
			}
		}

		try {
			String query = "select employee_id from employee where name= ?";
			empStatement = con.prepareStatement(query);
			empStatement.setString(1, name);
			resultSet = empStatement.executeQuery();
			if (!resultSet.next())
				return false;
			if (resultSet.next())
				empId = resultSet.getInt("employee_id");
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				return false;
			} catch (SQLException e1) {
				throw new EmployeePayRollException(e.getMessage());
			}
		}

		try {
			String query = "update payroll set deductions=0.2*basic_pay,taxable_pay=basic_pay-deductions,tax=0.1*taxable_pay,net_pay=taxable_pay-tax where employee_id=?";
			empStatement = con.prepareStatement(query);
			empStatement.setInt(1, empId);
			int status = empStatement.executeUpdate();
			if (status == 0)
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				return false;
			} catch (SQLException e1) {
				throw new EmployeePayRollException(e.getMessage());
			}
		}
		try {
			con.commit();
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw new EmployeePayRollException(e.getMessage());
			}
		}
		return true;
	}

	// retrieve all employee pay-roll details who started between certain date range
	public List<EmployeePayRoll> retrieveEmployeePayRollDetails(String start, String end)
			throws EmployeePayRollException {
		List<EmployeePayRoll> empList = new ArrayList<>();
		try (Connection con = PayRollDatabaseConnector.getConnection()) {
			empStatement = con.prepareStatement(
					"select * from employee e join payroll p on e.employee_id=p.employee_id where start_date between cast(? as date) and cast(? as date) and e.is_active=true");
			empStatement.setString(1, start);
			empStatement.setString(2, end);
			empList = getDataInDB();
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return empList;
	}

	// deletes employee payRoll details from payroll table
	public void deleteEmployeePayRollFromPayRollTable(String name) throws EmployeePayRollException {
		int employeeId = -1;
		try (Connection con = PayRollDatabaseConnector.getConnection()) {
			empStatement = con.prepareStatement("select employee_id from employee where name=?");
			empStatement.setString(1, name);
			resultSet = empStatement.executeQuery();
			while (resultSet.next())
				employeeId = resultSet.getInt("employee_id");
			empStatement = con.prepareStatement("delete from payroll where employee_id=?");
			empStatement.setInt(1, employeeId);
			int rowsAffected = empStatement.executeUpdate();
			if (rowsAffected > 0) {
				empStatement = con.prepareStatement("update employee set is_active=? where name=?");
				empStatement.setBoolean(1, false);
				empStatement.setString(2, name);
				empStatement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
	}

	// returns employee payroll data in database
	private List<EmployeePayRoll> getDataInDB() throws EmployeePayRollException {
		List<EmployeePayRoll> empList = new ArrayList<>();
		try {
			resultSet = empStatement.executeQuery();
			while (resultSet.next()) {
				empList.add(new EmployeePayRoll(resultSet.getString("employee_id"), resultSet.getString("name"),
						resultSet.getLong("basic_pay"), resultSet.getString("gender").charAt(0)));
			}
		} catch (SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return empList;
	}
}
