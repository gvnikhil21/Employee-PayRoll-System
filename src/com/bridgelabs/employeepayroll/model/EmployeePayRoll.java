package com.bridgelabs.employeepayroll.model;

public class EmployeePayRoll {
	private String empId;
	private String empName;
	private long empSalary;

	// no-argument constructor
	public EmployeePayRoll() {
	}

	// parameterized constructor
	public EmployeePayRoll(String empId, String empName, long empSalary) {
		this.empId = empId;
		this.empName = empName;
		this.empSalary = empSalary;
	}

	// getters and setters
	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public long getEmpSalary() {
		return empSalary;
	}

	public void setEmpSalary(long empSalary) {
		this.empSalary = empSalary;
	}

	@Override
	public String toString() {
		return "Employee ID: " + empId + " Employee Name: " + empName + " Employee Salary: " + empSalary;
	}
}
