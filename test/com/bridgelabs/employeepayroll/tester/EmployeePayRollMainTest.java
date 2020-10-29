package com.bridgelabs.employeepayroll.tester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.bridgelabs.employeepayroll.controller.*;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;

public class EmployeePayRollMainTest {

	@Test
	public void givenThreeEmployees_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
		EmployeePayRoll[] empArray = { new EmployeePayRoll("1", "Bill", 1000000),
				new EmployeePayRoll("3", "Bezos", 2000000), new EmployeePayRoll("3", "Musk", 3000000) };
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain(Arrays.asList(empArray));
		Integer entriesCount = employeePayRollMain.employeePayRollList.size();
		try {
			employeePayRollMain.writeEmployeePayRollDetails(EmployeePayRollMain.IOService.FILE_IO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(Integer.valueOf(3), entriesCount);
	}

	@Test
	public void givenThreeEmployees_WhenReadFromFile_ShouldMatchEmployeeEntries() {
		EmployeePayRoll[] empArray = { new EmployeePayRoll("1", "Bill", 1000000),
				new EmployeePayRoll("3", "Bezos", 2000000), new EmployeePayRoll("3", "Musk", 3000000) };
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain(Arrays.asList(empArray));
		Integer entriesCount = 0;
		try {
			employeePayRollMain.writeEmployeePayRollDetails(EmployeePayRollMain.IOService.FILE_IO);
			employeePayRollMain.readEmployeePayRollDetails(EmployeePayRollMain.IOService.FILE_IO);
			entriesCount = employeePayRollMain.employeePayRollList.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(Integer.valueOf(3), entriesCount);
	}

	@Test
	public void givenFourEmployeesInDB_ShouldMatchEmployeeEntries() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		Integer entriesCount = 0;
		try {
			employeePayRollMain.readEmployeePayRollDetails(EmployeePayRollMain.IOService.DB_IO);
			entriesCount = employeePayRollMain.employeePayRollList.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(Integer.valueOf(4), entriesCount);
	}

	@Test
	public void updatedBasePayForTerissa_ShouldRetrurnUpdatedBasePay() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		boolean response=false;
		try {
			response = employeePayRollMain.updateEmployeePayRollDetails(EmployeePayRollMain.IOService.DB_IO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(response);
	}
}
