package com.ltitraing.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ltitraing.data.Student;

public class StudentRepository {
	
	static List<Student> studentList; 
	static {
		System.out.println("In static block");
		studentList = new ArrayList<>();
		Student s1 = new Student("Arichi", 2, 2.2, "Female");
		Student s2 = new Student("Hardi", 2, 2.2, "Female");
		Student s3 = new Student("Abhishek", 3, 2.5, "male");
		Student s4 = new Student("jaymin", 1, 2.7, "male");
		Student s5 = new Student("Ranjan", 1, 3, "Male");
		studentList.addAll(Arrays.asList(s1, s2, s3, s4, s5));
	}
	
	public static List<Student> getStudents() {
		System.out.println("In static method");
		return new ArrayList<>(studentList);
	}

}
