package com.upper6.studentcourse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.upper6.studentcourse.mapper")
public class StudentcourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentcourseApplication.class, args);
	}

}
