package com.bridgelabs.employeepayroll.tester;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bridgelabs.employeepayroll.controller.*;
import com.bridgelabs.employeepayroll.controller.EmployeePayRollMain.IOService;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeePayRollMainTest {

	@Test
	public void test1_givenThreeEmployees_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
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
	public void test2_givenThreeEmployees_WhenReadFromFile_ShouldMatchEmployeeEntries() {
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
	public void test3_givenFourEmployeesInDB_ShouldMatchEmployeeEntries() {
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
	public void test4_updatedBasePayForTerissa_ShouldRetrurnUpdatedBasePay() {
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
	public void test5_retrieveData_GivenParticularDateRange_ShouldReturnCorrectEntries() {
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
	public void test6_calculatedAvgSalaryByGenderFromDB_ShouldSyncWithAvgSalaryByGenderFromList() {
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
			actualFemaleSalary = (empList.stream().filter(emp -> emp.getGender() == 'F' && emp.isActive())
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2))
					/ empList.stream().filter(emp -> emp.getGender() == 'F' && emp.isActive()).count();
			actualMaleSalary = (empList.stream().filter(emp -> emp.getGender() == 'M' && emp.isActive())
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2))
					/ empList.stream().filter(emp -> emp.getGender() == 'M' && emp.isActive()).count();
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(femaleAvgSalary, actualFemaleSalary);
		assertEquals(maleAvgSalary, actualMaleSalary);
	}

	@Test
	public void test7_calculatedTotalSalaryByGenderFromDB_ShouldSyncWithTotalSalaryByGenderFromList() {
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
			actualFemaleSalary = empList.stream().filter(emp -> emp.getGender() == 'F' && emp.isActive())
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2);
			actualMaleSalary = empList.stream().filter(emp -> emp.getGender() == 'M' && emp.isActive())
					.mapToLong(EmployeePayRoll::getEmpSalary).reduce(0, (emp1, emp2) -> emp1 + emp2);
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(femaleSalary, actualFemaleSalary);
		assertEquals(maleSalary, actualMaleSalary);
	}

	@Test
	public void test8_calculatedTotalEMployeesyByGenderFromDB_ShouldSyncWithTotalEmployeesByGenderFromList() {
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
			actualFemaleCount = empList.stream().filter(emp -> emp.getGender() == 'F' && emp.isActive()).count();
			actualMaleCount = empList.stream().filter(emp -> emp.getGender() == 'M' && emp.isActive()).count();
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(femaleCount, actualFemaleCount);
		assertEquals(maleCount, actualMaleCount);
	}

	@Test
	public void test90_givenNewEmployee_WhenAdded_ShouldBeInSyncWithDB() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		EmployeePayRoll employeePayRoll = null;
		String DepartmentId[] = { "1", "3" };
		EmployeePayRoll actualEmployeePayRoll = new EmployeePayRoll("Bezos", 5000000, 'M', LocalDate.now(), "1",
				DepartmentId);
		try {
			employeePayRollMain.addEmployeePayRollDetailsToDB(actualEmployeePayRoll);
			employeePayRoll = employeePayRollMain.employeePayRollList.stream()
					.filter(emp -> emp.getEmpName().equals("Bezos")).findFirst().orElse(null);
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(employeePayRoll, actualEmployeePayRoll);
	}

	@Test
	public void test91_givenEmployeeName_WhenDeleted_ShouldBeInSyncWithDB() {
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain();
		EmployeePayRoll employeePayRoll = null;
		try {
			employeePayRollMain.deleteEmployeePayRollFromPayRollTableAndList("Bezos");
			employeePayRoll = employeePayRollMain.employeePayRollList.stream()
					.filter(emp -> emp.getEmpName().equals("Bezos")).findFirst().orElse(null);
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(null, employeePayRoll);
	}
}
