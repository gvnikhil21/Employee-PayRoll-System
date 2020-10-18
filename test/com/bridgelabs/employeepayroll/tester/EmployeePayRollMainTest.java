package com.bridgelabs.employeepayroll.tester;

import static org.junit.Assert.assertEquals;

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
		try {
			employeePayRollMain.writeEmployeePayRollDetails(EmployeePayRollMain.IOService.FILE_IO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Long entriesCount = employeePayRollMain.countEntries(EmployeePayRollMain.IOService.FILE_IO);
		assertEquals(Long.valueOf(3), entriesCount);
	}

}
