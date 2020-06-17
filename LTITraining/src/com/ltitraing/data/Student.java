package com.ltitraing.data;

public class Student {
	
	private String name;
	private int grade;
	private double gpa;
	private String gender;
	private String result;
	
	@Override
	public String toString() {
		return "Student [name=" + name + ", grade=" + grade + ", gpa=" + gpa + ",result=" + result + ", gender=" + gender + "]";
	}
	public Student(String name, int grade, double gpa, String gender) {
		super();
		this.name = name;
		this.grade = grade;
		this.gpa = gpa;
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public double getGpa() {
		return gpa;
	}
	public void setGpa(double gpa) {
		this.gpa = gpa;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

}
