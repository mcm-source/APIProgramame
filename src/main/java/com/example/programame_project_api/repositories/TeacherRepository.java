package com.example.programame_project_api.repositories;

import com.example.programame_project_api.entities.Teacher;
import org.springframework.data.repository.CrudRepository;

public interface TeacherRepository extends CrudRepository<Teacher,Long> {

    Teacher findByEmail(String email);
}
