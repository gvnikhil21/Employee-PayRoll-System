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
		Long entriesCount = 0l;
		try {
			employeePayRollMain.writeEmployeePayRollDetails(EmployeePayRollMain.IOService.FILE_IO);
			entriesCount = employeePayRollMain.countEntries(EmployeePayRollMain.IOService.FILE_IO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(Long.valueOf(3), entriesCount);
	}

}
