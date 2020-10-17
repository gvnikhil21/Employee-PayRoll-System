package com.bridgelabs.employeepayroll.controller;

import java.io.*;

public class EmployeePayRollMain {

	public static void main(String[] args) {
		BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		try {
			consoleWriter.write("Welcome to Employee Payroll System!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				consoleWriter.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
}
