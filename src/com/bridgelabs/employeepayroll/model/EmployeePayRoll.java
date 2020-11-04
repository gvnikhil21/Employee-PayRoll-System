package com.bridgelabs.employeepayroll.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class EmployeePayRoll {
	private String empId;
	private String empName;
	private Long empSalary;
	private Character gender;
	private LocalDate startDate;
	private String companyId;
	private String department[];
	private boolean isActive = true;

	// no-argument constructor
	public EmployeePayRoll() {
	}

	// parameterized constructor
	public EmployeePayRoll(String empName, long empSalary) {
		this.empName = empName;
		this.empSalary = empSalary;
	}

	public EmployeePayRoll(String empId, String empName, long empSalary) {
		this(empName, empSalary);
		this.empId = empId;
	}

	public EmployeePayRoll(String empId, String empName, long empSalary, char gender) {
		this(empId, empName, empSalary);
		this.gender = gender;
	}

	public EmployeePayRoll(String empId, String empName, long empSalary, char gender, LocalDate startDate,
			String companyId) {
		this(empId, empName, empSalary, gender);
		this.startDate = startDate;
		this.companyId = companyId;
	}

	public EmployeePayRoll(String empId, String empName, long empSalary, char gender, LocalDate startDate,
			String companyId, String[] department) {
		this(empId, empName, empSalary, gender, startDate, companyId);
		this.department = department;
	}

	public EmployeePayRoll(String empName, long empSalary, char gender, LocalDate startDate, String companyId) {
		this.empName = empName;
		this.empSalary = empSalary;
		this.gender = gender;
		this.startDate = startDate;
		this.companyId = companyId;
	}

	public EmployeePayRoll(String empName, long empSalary, char gender, LocalDate startDate, String companyId,
			String[] department) {
		this(empName, empSalary, gender, startDate, companyId);
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

	public Long getEmpSalary() {
		return empSalary;
	}

	public void setEmpSalary(Long empSalary) {
		this.empSalary = empSalary;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
	public int hashCode() {
		return Objects.hash(empName, gender, empSalary, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePayRoll other = (EmployeePayRoll) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (!Arrays.equals(department, other.department))
			return false;
		if (empId == null) {
			if (other.empId != null)
				return false;
		} else if (!empId.equals(other.empId))
			return false;
		if (empName == null) {
			if (other.empName != null)
				return false;
		} else if (!empName.equals(other.empName))
			return false;
		if (empSalary != other.empSalary)
			return false;
		if (gender != other.gender)
			return false;
		if (isActive != other.isActive)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Employee ID: " + empId + " Employee Name: " + empName + " Employee Salary: " + empSalary;
	}
}
