package com.seun.pree6.repository;

import com.seun.pree6.entity.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {

}
