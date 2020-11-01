package com.bridgelabs.employeepayroll.tester;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.bridgelabs.employeepayroll.controller.*;
import com.bridgelabs.employeepayroll.controller.EmployeePayRollMain.IOService;
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
			employeePayRollMain.writeEmployeePayRollDetails(IOService.FILE_IO);
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
			employeePayRollMain.writeEmployeePayRollDetails(IOService.FILE_IO);
			employeePayRollMain.readEmployeePayRollDetails(IOService.FILE_IO);
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
			employeePayRollMain.readEmployeePayRollDetails(IOService.DB_IO);
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
			employeePayRollMain.updateEmployeePayRollDetails(IOService.DB_IO, 3000000l, "Terissa");
			employeePayRollMain.readEmployeePayRollDetails(IOService.DB_IO);
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

	@Test
	public void calculatedAvgSalaryByGenderFromDB_ShouldSyncWithAvgSalaryByGenderFromList() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		Long femaleAvgSalary = 0l;
		Long actualFemaleSalary = 0l;
		Long maleAvgSalary = 0l;
		Long actualMaleSalary = 0l;
		try {
			employeePayRollMain.readEmployeePayRollDetails(IOService.DB_IO);
			List<EmployeePayRoll> empList = employeePayRollMain.employeePayRollList;
			femaleAvgSalary = employeePayRollMain.getAvgSalaryByGenderFromDB('F');
			maleAvgSalary = employeePayRollMain.getAvgSalaryByGenderFromDB('M');
			actualFemaleSalary = (empList.stream().filter(emp -> emp.getGender() == 'F')
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2))
					/ empList.stream().filter(emp -> emp.getGender() == 'F').count();
			actualMaleSalary = (empList.stream().filter(emp -> emp.getGender() == 'M')
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2))
					/ empList.stream().filter(emp -> emp.getGender() == 'M').count();
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(femaleAvgSalary, actualFemaleSalary);
		assertEquals(maleAvgSalary, actualMaleSalary);
	}

	@Test
	public void calculatedTotalSalaryByGenderFromDB_ShouldSyncWithTotalSalaryByGenderFromList() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		Long femaleSalary = 0l;
		Long actualFemaleSalary = 0l;
		Long maleSalary = 0l;
		Long actualMaleSalary = 0l;
		try {
			employeePayRollMain.readEmployeePayRollDetails(IOService.DB_IO);
			List<EmployeePayRoll> empList = employeePayRollMain.employeePayRollList;
			femaleSalary = employeePayRollMain.getTotalSalaryByGenderFromDB('F');
			maleSalary = employeePayRollMain.getTotalSalaryByGenderFromDB('M');
			actualFemaleSalary = empList.stream().filter(emp -> emp.getGender() == 'F')
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2);
			actualMaleSalary = empList.stream().filter(emp -> emp.getGender() == 'M')
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2);
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(femaleSalary, actualFemaleSalary);
		assertEquals(maleSalary, actualMaleSalary);
	}

	@Test
	public void calculatedTotalEMployeesyByGenderFromDB_ShouldSyncWithTotalEmployeesByGenderFromList() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		Long femaleCount = 0l;
		Long actualFemaleCount = 0l;
		Long maleCount = 0l;
		Long actualMaleCount = 0l;
		try {
			employeePayRollMain.readEmployeePayRollDetails(IOService.DB_IO);
			List<EmployeePayRoll> empList = employeePayRollMain.employeePayRollList;
			femaleCount = employeePayRollMain.getCountOfEmployeesByGenderFromDB('F');
			maleCount = employeePayRollMain.getCountOfEmployeesByGenderFromDB('M');
			actualFemaleCount = empList.stream().filter(emp -> emp.getGender() == 'F').count();
			actualMaleCount = empList.stream().filter(emp -> emp.getGender() == 'M').count();
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(femaleCount, actualFemaleCount);
		assertEquals(maleCount, actualMaleCount);
	}
}
