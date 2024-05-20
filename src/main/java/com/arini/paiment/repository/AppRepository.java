
package com.arini.paiment.repository;


import com.arini.paiment.model.ClassRoom;
import com.arini.paiment.model.Err;
import com.arini.paiment.model.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AppRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<ClassRoom> classroomRowMapper = new RowMapper<ClassRoom>() {
        @Override
        public ClassRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClassRoom classroom = new ClassRoom();
            classroom.setClassroomId(UUID.fromString(rs.getString("classroom_id")));
            classroom.setTitle(rs.getString("title"));
            classroom.setArabicTitle(rs.getString("arabic_title"));
            classroom.setPrice(rs.getFloat("price"));
            return classroom;
        }
    };

    private RowMapper<Student> studentRowMapper = new RowMapper<Student>() {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student student = new Student();
            student.setStudentID(UUID.fromString(rs.getString("student_id")));
            student.setFirstName(rs.getString("firstname"));
            student.setAmount(rs.getFloat("amount"));
            return student;
        }
    };

    private RowMapper<Boolean> booleanRowMapper = new RowMapper<Boolean>() {
        @Override
        public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getBoolean(1);
        }
    };


    public Optional<ClassRoom> findClassRoomByID(UUID id) {
        String FIND_CLASSROOM_QUERY = "SELECT classroom_id,title,arabic_title,price FROM classrooms WHERE classroom_id = ?;";
        return jdbcTemplate.query(FIND_CLASSROOM_QUERY, new Object[]{id}, classroomRowMapper)
                .stream().findFirst();
    }

    public Optional<Student> findStudentByID(UUID id) {
        String FIND_STUDENT_QUERY = "SELECT student_id,firstname,amount FROM students WHERE student_id = ? LIMIT 1;";
        return jdbcTemplate.query(FIND_STUDENT_QUERY, new Object[]{id}, studentRowMapper)
                .stream().findFirst();
    }

    public Err updateStudent(Student student) {
        String sql = "UPDATE students SET amount = ? WHERE student_id = ?;";
        try {
            jdbcTemplate.update(sql, student.getAmount(), student.getStudentID());
            return null;
        } catch (DataAccessException e) {
            return new Err(e.getMessage());
        }

    }
}