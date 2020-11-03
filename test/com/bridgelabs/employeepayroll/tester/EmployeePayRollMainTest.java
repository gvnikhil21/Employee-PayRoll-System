package com.bridgelabs.employeepayroll.tester;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bridgelabs.employeepayroll.controller.*;
import com.bridgelabs.employeepayroll.controller.EmployeePayRollMain.IOService;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeePayRollMainTest {
	private static EmployeePayRollMain employeePayRollMain;

	@Before
	public void init() {
		employeePayRollMain = new EmployeePayRollMain();
	}

	@Test
	public void test1_givenThreeEmployees_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
		EmployeePayRoll[] empArray = { new EmployeePayRoll("1", "Bill", 1000000),
				new EmployeePayRoll("3", "Bezos", 2000000), new EmployeePayRoll("3", "Musk", 3000000) };
		EmployeePayRollMain employeePayRollMainOne = new EmployeePayRollMain(Arrays.asList(empArray));
		Integer entriesCount = employeePayRollMainOne.employeePayRollList.size();
		try {
			employeePayRollMainOne.writeEmployeePayRollDetails(IOService.FILE_IO);
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(Integer.valueOf(3), entriesCount);
	}

	@Test
	public void test2_givenThreeEmployees_WhenReadFromFile_ShouldMatchEmployeeEntries() {
		EmployeePayRoll[] empArray = { new EmployeePayRoll("1", "Bill", 1000000),
				new EmployeePayRoll("3", "Bezos", 2000000), new EmployeePayRoll("3", "Musk", 3000000) };
		EmployeePayRollMain employeePayRollMainOne = new EmployeePayRollMain(Arrays.asList(empArray));
		Integer entriesCount = 0;
		try {
			employeePayRollMainOne.writeEmployeePayRollDetails(IOService.FILE_IO);
			employeePayRollMainOne.readEmployeePayRollDetails(IOService.FILE_IO);
			entriesCount = employeePayRollMainOne.employeePayRollList.size();
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		assertEquals(Integer.valueOf(3), entriesCount);
	}

	@Test
	public void test3_givenFourEmployeesInDB_ShouldMatchEmployeeEntries() {
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

	@Test
	public void test92_givenMultipleEmployess_WhenAdded_ShouldBeInSyncWithDB() {
		EmployeePayRoll emp[] = {
				new EmployeePayRoll("Chris", 5000000l, 'M', LocalDate.of(2018, 06, 15), "2", "1,4".split(",")),
				new EmployeePayRoll("Christine", 6000000l, 'F', LocalDate.now(), "4", "2".split(",")),
				new EmployeePayRoll("Henry", 7000000l, 'M', LocalDate.of(2017, 06, 15), "2", "4".split(",")),
				new EmployeePayRoll("Helen", 5000000l, 'F', LocalDate.of(2019, 06, 15), "4", "3".split(",")) };
		try {
			employeePayRollMain.readEmployeePayRollDetails(IOService.DB_IO);
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		Instant start = Instant.now();
		employeePayRollMain.addEmployeePayRollDetailsToDBWithOutThread(Arrays.asList(emp));
		Instant end = Instant.now();
		EmployeePayRollMain.LOG.info("Duration without thread: " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayRollMain.addEmployeePayRollDetailsToDBWithThreads(Arrays.asList(emp));
		Instant threadEnd = Instant.now();
		EmployeePayRollMain.LOG.info("Duration with threads: " + Duration.between(threadStart, threadEnd));
		assertEquals(12, employeePayRollMain.employeePayRollList.size());
	}
}