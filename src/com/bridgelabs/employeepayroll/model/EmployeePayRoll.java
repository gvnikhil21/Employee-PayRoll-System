package com.bridgelabs.employeepayroll.model;

import java.time.LocalDate;

public class EmployeePayRoll {
	private String empId;
	private String empName;
	private long empSalary;
	private char gender;
	private LocalDate startDate;
	private String company_id;
	private String department[];
	private boolean isActive = true;

	// no-argument constructor
	public EmployeePayRoll() {
	}

	// parameterized constructor
	public EmployeePayRoll(String empId, String empName, long empSalary) {
		this.empId = empId;
		this.empName = empName;
		this.empSalary = empSalary;
	}

	public EmployeePayRoll(String empId, String empName, long empSalary, char gender) {
		this(empId, empName, empSalary);
		this.gender = gender;
	}

	public EmployeePayRoll(String empId, String empName, long empSalary, char gender, LocalDate startDate,
			String company_id) {
		this(empId, empName, empSalary, gender);
		this.startDate = startDate;
		this.company_id = company_id;
	}

	public EmployeePayRoll(String empId, String empName, long empSalary, char gender, LocalDate startDate,
			String company_id, String[] department) {
		this(empId, empName, empSalary, gender, startDate, company_id);
		this.department = department;
	}

	public EmployeePayRoll(String empName, long empSalary, char gender, LocalDate startDate, String company_id) {
		this.empName = empName;
		this.empSalary = empSalary;
		this.gender = gender;
		this.startDate = startDate;
		this.company_id = company_id;
	}

	public EmployeePayRoll(String empName, long empSalary, char gender, LocalDate startDate, String company_id,
			String[] department) {
		this(empName, empSalary, gender, startDate, company_id);
		this.department = department;
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

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String[] getDepartment() {
		return department;
	}

	public void setDepartment(String[] department) {
		this.department = department;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Employee ID: " + empId + " Employee Name: " + empName + " Employee Salary: " + empSalary;
	}
}
