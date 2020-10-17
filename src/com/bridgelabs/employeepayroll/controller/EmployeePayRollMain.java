package com.bridgelabs.employeepayroll.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabs.employeepayroll.model.*;

public class EmployeePayRollMain {
	private List<EmployeePayRoll> employeePayRollList;

	public EmployeePayRollMain() {
		employeePayRollList = new ArrayList<EmployeePayRoll>();
	}

	public static void main(String[] args) {
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		try {
			consoleWriter.write("Welcome to Employee Payroll System!\n");
			consoleWriter.flush();
			employeePayRollMain.readEmployeePayRollDetails(consoleWriter, consoleReader);
			employeePayRollMain.writeEmployeePayRollDetails(consoleWriter);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				consoleWriter.close();
				consoleReader.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	// reads employee payroll details from console
	private void readEmployeePayRollDetails(BufferedWriter consoleWriter, BufferedReader consoleReader)
			throws IOException {
		consoleWriter.write("Enter the employee Id: \n");
		consoleWriter.flush();
		String id = consoleReader.readLine();
		consoleWriter.write("Enter the employee name: \n");
		consoleWriter.flush();
		String name = consoleReader.readLine();
		consoleWriter.write("Enter the employee salary: \n");
		consoleWriter.flush();
		Long salary = Long.parseLong(consoleReader.readLine());
		EmployeePayRoll employeePayRoll = new EmployeePayRoll(id, name, salary);
		employeePayRollList.add(employeePayRoll);
	}

	// writes employee payroll details to console
	private void writeEmployeePayRollDetails(BufferedWriter consoleWriter) throws IOException {
		consoleWriter.write(employeePayRollList.toString());
	}
}
