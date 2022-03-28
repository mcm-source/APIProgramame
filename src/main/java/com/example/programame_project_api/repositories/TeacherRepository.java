package com.example.programame_project_api.repositories;

import com.example.programame_project_api.entities.persistEntities.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface TeacherRepository extends CrudRepository<Teacher,Long> {

    boolean existsById(long id);
    Teacher findByEmail(String email);
    Teacher findById(long id);
    List<Teacher> findAll();

    public default void update(Map<String, Object> user, String oldEmail){

        Teacher teacherData = findByEmail(oldEmail);
        teacherData.setEmail( (String) user.get("email"));
        teacherData.setName((String) user.get("name"));
        save(teacherData);

    }
}
