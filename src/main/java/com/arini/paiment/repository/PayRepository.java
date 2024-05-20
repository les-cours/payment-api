package com.arini.paiment.repository;


import com.arini.paiment.model.Err;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class PayRepository {

    private final JdbcTemplate jdbcTemplate;

    public PayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private RowMapper<Boolean> booleanRowMapper = new RowMapper<Boolean>() {
        @Override
        public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getBoolean(1);
        }
    };




    public Err CheckPurchased(UUID studentID, UUID classroomID, String month) {
        String sql = "SELECT EXISTS(SELECT 1 FROM  subscriptions WHERE classroom_id = ? AND student_id = ? LIMIT 1 ;";
        var rs = jdbcTemplate.query(sql, new Object[]{classroomID,studentID}, booleanRowMapper).stream().findFirst();

        if (!rs.isPresent()) {
            return new Err("err 500.");
        }

       if ( rs.get() ) {
           return  new Err("already purchased.");
       }
       return null;
    }

    public Err CreateSubscription(UUID studentID, UUID classroomID, String month) {
        String sql = "INSERT INTO subcriptions (student_id, classroom_id, ?) VALUES (?, ?,true)";
        try {
             jdbcTemplate.update(sql, studentID, classroomID,month);
        }catch (DataAccessException e) {
            return new Err(e.getMessage());
        }


        return null;
    }

    public Err payMonth(UUID studentID, UUID classroomID, String month) {
        String sql = "UPDATE subscriptions SET ? = true WHERE classroom_id = ? AND student_id = ?;";
        try {
            jdbcTemplate.update(sql,month, studentID, classroomID );
        }catch (DataAccessException e) {
            return new Err(e.getMessage());
        }


        return null;
    }
}
