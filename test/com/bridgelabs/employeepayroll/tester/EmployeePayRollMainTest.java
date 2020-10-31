package com.bridgelabs.employeepayroll.tester;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.bridgelabs.employeepayroll.controller.*;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

public class EmployeePayRollMainTest {

	@Test
	public void givenThreeEmployees_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
		EmployeePayRoll[] empArray = { new EmployeePayRoll("1", "Bill", 1000000),
				new EmployeePayRoll("3", "Bezos", 2000000), new EmployeePayRoll("3", "Musk", 3000000) };
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain(Arrays.asList(empArray));
		Integer entriesCount = employeePayRollMain.employeePayRollList.size();
		try {
			employeePayRollMain.writeEmployeePayRollDetails(EmployeePayRollMain.IOService.FILE_IO);
		} catch (EmployeePayRollException e) {
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
		} catch (EmployeePayRollException e) {
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
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(Integer.valueOf(4), entriesCount);
	}

	@Test
	public void updatedBasePayForTerissa_ShouldRetrurnUpdatedBasePay() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		EmployeePayRoll employee = null;
		try {
			employeePayRollMain.updateEmployeePayRollDetails(EmployeePayRollMain.IOService.DB_IO, 3000000l, "Terissa");
			employeePayRollMain.readEmployeePayRollDetails(EmployeePayRollMain.IOService.DB_IO);
			List<EmployeePayRoll> empList = employeePayRollMain.employeePayRollList;
			employee = empList.stream().filter(emp -> emp.getEmpName().equalsIgnoreCase("Terissa")).findFirst()
					.orElse(null);
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(Long.valueOf(3000000), Long.valueOf(employee.getEmpSalary()));
	}

	@Test
	public void retrieveData_GivenParticularDateRange_ShouldReturnCorrectEntries() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		Integer entriesCount = 0;
		try {
			List<EmployeePayRoll> empList = employeePayRollMain.getEmployeePayRollDataForParticularDates("2013-01-01",
					"2015-12-31");
			entriesCount = empList.size();
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(Integer.valueOf(3), entriesCount);
	}
}
