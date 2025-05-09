package com.project.model;

import java.time.LocalDate;

public class Employee  {

	private int code;
	private String name;
	private int age;
	private LocalDate dob;
	
	public Employee(int code, String name, int age, LocalDate dob) {
		this.code = code;
		this.name = name;
		this.age = age;
		this.dob = dob;
	}
	
	public Employee(String name, int age, LocalDate dob) {
		this.name = name;
		this.age = age;
		this.dob = dob;
	}
	
	public Employee() {
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public LocalDate getDob() {
		return dob;
	}
	@Override
	public String toString() {
		return "Employee [code=" + code + ", name=" + name + ", age=" + age + ", dob=" + dob + "]";
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	
	
}
