package com.bridgelabs.employeepayroll.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.bridgelabs.employeepayroll.controller.EmployeePayRollMain;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

public class PayRollDatabaseConnector {
	private static Connection con = null;

	// connects to the payroll_service database
	public static Connection getConnection() throws EmployeePayRollException {
		String url = "jdbc:mysql://localhost:3306/payroll_service";
		String user = "root";
		String password = "root";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			EmployeePayRollMain.LOG.info("Connection established...");
		} catch (ClassNotFoundException | SQLException e) {
			throw new EmployeePayRollException(e.getMessage());
		}
		return con;
	}
}
