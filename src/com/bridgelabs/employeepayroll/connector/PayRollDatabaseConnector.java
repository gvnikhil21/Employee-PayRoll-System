package com.bridgelabs.employeepayroll.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.bridgelabs.employeepayroll.controller.EmployeePayRollMain;

public class PayRollDatabaseConnector {
	private static Connection con = null;

	// connects to the payroll_service database
	public static Connection getConnection() {
		String url = "jdbc:mysql://localhost:3306/payroll_service";
		String user = "root";
		String password = "root";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			EmployeePayRollMain.LOG.info("Connection established...");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
