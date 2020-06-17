package com.ltitraining.main;

import java.util.List;

import com.ltitraing.data.Student;
import com.ltitraing.repo.StudentRepository;

public class MainApp {

	public static void main(String[] args) {
		
		List<Student> studentList = StudentRepository.getStudents();
		System.out.println(studentList);
		studentList.stream().filter(student -> student.getGpa() > 2.5).map(
				student -> {
					student.setResult("Excellent");
					return student;
				}).forEach(System.out::println);
		System.out.println(studentList);

	}

}
