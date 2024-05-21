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
public class ChargeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChargeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private RowMapper<Boolean> booleanRowMapper = new RowMapper<Boolean>() {
        @Override
        public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getBoolean(1);
        }
    };

    private RowMapper<Float> floatRowMapper = new RowMapper<Float>() {
        @Override
        public Float mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getFloat(1);
        }
    };

    public Err SavePaymentCode(String code, float amount) {

        try {
            jdbcTemplate.update("insert into payment_codes (code, amount) values (?, ?)", code, amount);
            return null;
        }catch (DataAccessException e){
            return new Err(e.getMessage());
        }
    }


    public Err UpdateStudent(Student student) {
        String sql = "UPDATE students SET amount = ? WHERE student_id = ?;";
        try{
            jdbcTemplate.update(sql, student.getAmount(), student.getStudentID());
            return null;
        }catch (DataAccessException e){
           return new Err(e.getMessage());
        }

    }

    public Err CheckPurchased(UUID studentID, UUID classroomID, String month) {
        String sql = "SELECT EXISTS(SELECT 1 FROM  subscriptions WHERE classroom_id = ? AND student_id = ? LIMIT 1 ;";
        var rs = jdbcTemplate.query(sql, new Object[]{classroomID,studentID}, booleanRowMapper).stream().findFirst();

        if (rs.isEmpty()) {
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

    public Float getAmountByPaymentCode(String paymentCode) {

        String FIND_STUDENT_QUERY = "select amount from payment_codes where code = ?;";//and used = false;
         var rs =  jdbcTemplate.query(FIND_STUDENT_QUERY, new Object[]{paymentCode}, floatRowMapper)
                .stream().findFirst();
        return rs.orElse(null);

    }
}
