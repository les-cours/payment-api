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

    public Float getAmountByPaymentCode(String paymentCode) {

        String FIND_STUDENT_QUERY = "select amount from payment_codes where code = ? AND used_at IS NULL;";
         var rs =  jdbcTemplate.query(FIND_STUDENT_QUERY, new Object[]{paymentCode}, floatRowMapper)
                .stream().findFirst();
        return rs.orElse(null);

    }


}
