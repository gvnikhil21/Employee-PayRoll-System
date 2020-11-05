package com.bridgelabs.employeepayroll.tester;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.*;

import com.bridgelabs.employeepayroll.controller.EmployeePayRollMain;
import com.bridgelabs.employeepayroll.controller.EmployeePayRollMain.IOService;
import com.bridgelabs.employeepayroll.model.EmployeePayRoll;
import com.bridgelabs.employeepayroll.model.EmployeePayRollException;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayRollRESTAPITest {
	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	@Test
	public void givenEmployee_WhenAdded_ShouldMatch201Response() {
		EmployeePayRoll[] empArray = getEmployeeList();
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain(Arrays.asList(empArray));
		EmployeePayRoll[] empPayRoll = { new EmployeePayRoll("2", "Piyush", 6000000l, 'M', LocalDate.now()),
				new EmployeePayRoll("3", "Nirmala", 7000000l, 'F', LocalDate.now()),
				new EmployeePayRoll("4", "Sonia", 6000000l, 'F', LocalDate.now()),
				new EmployeePayRoll("5", "Kalam", 9000000l, 'M', LocalDate.now()) };
		Arrays.asList(empPayRoll).stream().forEach(employeePayRoll -> {
			Response response = addEmployeeToJSONServer(employeePayRoll);
			int statusCode = response.getStatusCode();
			assertEquals(201, statusCode);
			employeePayRoll = new Gson().fromJson(response.asString(), EmployeePayRoll.class);
			try {
				employeePayRollMain.addEmployeePayRollDetails(employeePayRoll, IOService.REST_IO);
			} catch (EmployeePayRollException e) {
				e.printStackTrace();
			}
		});

		assertEquals(5, employeePayRollMain.employeePayRollList.size());
	}

	@Test
	public void givenEmployeeSalary_WhenUpdated_ShouldMatch() {
		EmployeePayRoll[] empPayRoll = getEmployeeList();
		EmployeePayRollMain employeePayRollMain = new EmployeePayRollMain(Arrays.asList(empPayRoll));
		try {
			employeePayRollMain.updateEmployeePayRollDetails(IOService.REST_IO, 6000000l, "Goyal");
		} catch (EmployeePayRollException e) {
			e.printStackTrace();
		}
		EmployeePayRoll employeePayRoll = employeePayRollMain.getEmployeePayRoll("Goyal");
		String Json = new Gson().toJson(employeePayRoll);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(Json);
		Response response = request.put("/employees/" + employeePayRoll.getEmpId());
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
		assertEquals(Long.valueOf(6000000), employeePayRoll.getEmpSalary());
	}

	private Response addEmployeeToJSONServer(EmployeePayRoll empPayRoll) {
		String Json = new Gson().toJson(empPayRoll);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(Json);
		return request.post("/employees");
	}

	private EmployeePayRoll[] getEmployeeList() {
		Response response = RestAssured.get("/employees");
		EmployeePayRollMain.LOG.info("Employee payroll details(in JSON server): \n" + response.getBody().asString());
		return new Gson().fromJson(response.asString(), EmployeePayRoll[].class);
	}
}
